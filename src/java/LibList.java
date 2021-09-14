/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.opensymphony.xwork2.ActionContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.SequenceInputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

/**
 *
 * @author Dave
 */
public class LibList extends ActionSupport implements Preparable {

    /* forward name="success" path="" */
    private Log logger = LogFactory.getLog(this.getClass());
    private Message message;
    private List messages;
    private Gallery gallery;
    private List galleries;
    private String error_mesgs = "";
    private File image;
    private LinkedList images;
    private InputStream inputStream = null;

    public void prepare() throws Exception {
    }

    public String list() throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store = null;
        try {
            store = session.getStore("pop3");
            Folder folder = getFolder(store);
            folder.open(Folder.READ_ONLY);
            SubjectTerm term = new SubjectTerm("gallery_");
            Message[] tmp = folder.search(term);
            messages = new LinkedList<Message>();
            galleries = new LinkedList<Gallery>();
            String subj;
            for (int i = 0; i < tmp.length; i++) {
                subj = tmp[i].getSubject();
                subj = subj.substring(8, subj.length());
//                tmp[i].setSubject(subj);
                Gallery tmp2 = new Gallery();
                tmp2.setTitle(subj);
                galleries.add(tmp2);
//                messages.add(tmp[i]);
            }
            folder.close(false);
            store.close();
            return SUCCESS;
        } catch (Exception ex) {
            if (store != null) {
                store.close();
            }
            return ERROR;
        }
    }

    public String newGallery() {
        return INPUT;
    }

    public String save() throws MessagingException {
        if (gallery != null) {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            Store store = null;
            try {
                store = session.getStore("pop3");
                MimeMessage mesg = new MimeMessage(session);
                mesg.setSubject("gallery_" + gallery.getTitle());
                Folder folder = getFolder(store);
                folder.open(Folder.READ_ONLY);
                SubjectTerm term = new SubjectTerm("gallery_" + gallery.getTitle());
                Message[] msgs = folder.search(term);
                store.close();
                if (msgs.length > 0) {
                    this.error_mesgs = "Taki tytuł już istnieje";
                    return INPUT;
                }
                mesg.setText(gallery.getDescription());
                sendMessage(mesg);
                return SUCCESS;
            } catch (Exception e) {
                if (store != null) {
                    store.close();
                }
                return ERROR;
            }
        }
        return INPUT;
    }

    public String show() throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store = null;
        try {
            store = session.getStore("pop3");
            Folder folder = getFolder(store);
            folder.open(Folder.READ_ONLY);
            Map parameters = ActionContext.getContext().getParameters();
            String[] id = (String[]) parameters.get("id");
            SubjectTerm term = new SubjectTerm("gallery_" + id[0]);
            Message[] msg = folder.search(term);
            if (msg != null) {
                this.gallery = new Gallery();
                this.gallery.setTitle(id[0]);
                this.gallery.setDescription((String) msg[0].getContent());
                AndTerm term2 = new AndTerm(new SubjectTerm("image_" + id[0] + "_"), new SubjectTerm("_last"));
                msg = folder.search(term2);
                LinkedList imgs = new LinkedList<Image>();
                Image tmp;
                for (int i = 0; i < msg.length; i++) {
                    tmp = new Image();
                    String fname = msg[i].getSubject();
                    tmp.setFilename(fname.substring(7 + id[0].length(), fname.lastIndexOf("_")));
                    tmp.setDate(msg[i].getSentDate().toString());
                    imgs.add(tmp);
                }
                this.gallery.setImages(imgs);
                this.images = imgs;
            }
            store.close();
        } catch (Exception e) {
            if (store != null) {
                store.close();
            }
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    public String edit() throws MessagingException {
        if (gallery != null) {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            Store store = null;
            try {
                store = session.getStore("pop3");
                Folder folder = getFolder(store);
                folder.open(Folder.READ_WRITE);
                SubjectTerm term = new SubjectTerm("gallery_" + gallery.getTitle());
                Message[] msgs = folder.search(term);
                folder.setFlags(msgs, new Flags(Flag.DELETED), true);
                folder.close(true);
                MimeMessage mesg = new MimeMessage(session);
                mesg.setSubject(msgs[0].getSubject());
                mesg.setText(gallery.getDescription());
                mesg.setSentDate(msgs[0].getSentDate());
                sendMessage(mesg);
                store.close();
                return SUCCESS;
            } catch (Exception e) {
                if (store != null) {
                    store.close();
                }
                return ERROR;
            }
        }
        Map params = ActionContext.getContext().getParameters();
        String id = ((String[]) params.get("id"))[0];
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store = null;
        try {
            store = session.getStore("pop3");
            Folder folder = getFolder(store);
            folder.open(Folder.READ_ONLY);
            SubjectTerm term = new SubjectTerm("gallery_" + id);
            Message[] msgs = folder.search(term);
            this.gallery = new Gallery();
            this.gallery.setTitle(id);
            this.gallery.setDescription((String) msgs[0].getContent());
            return INPUT;
        } catch (Exception e) {
            e.printStackTrace();
            if (store != null) {
                store.close();
            }
            return ERROR;
        }
    }

    public String showImage() throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store = null;
        try {
            store = session.getStore("pop3");
            Folder folder = getFolder(store);
            folder.open(Folder.READ_ONLY);
            Map parameters = ActionContext.getContext().getParameters();
            String[] id = (String[]) parameters.get("id");
            String[] id2 = (String[]) parameters.get("img_name");
            String image_name = "image_" + id[0] + "_" + id2[0];
            SubjectTerm term = new SubjectTerm(image_name);
            Message[] msg = folder.search(term);
            String file;
            Vector<InputStream> streams = new Vector<InputStream>();
            if (msg.length > 0) { // czyli cos bylo
                int ll = msg.length;
                for (int i = 0; i < ll; i++) {
                    term = new SubjectTerm(image_name + "_part" + i);
                    msg = folder.search(term);
                    if (msg.length == 0) {//nie bylo takiej czesci. wiec musi byc last
                        term = new SubjectTerm(image_name + "_last");
                        msg = folder.search(term);
                    } // tu cos juz musi byc
                    Multipart partm = (Multipart) msg[0].getContent();
                    Part part = partm.getBodyPart(0);
                    String disposition = part.getDisposition();
                    String contentType = part.getContentType();
                    streams.add(part.getInputStream());
                    if (this.inputStream != null) { // i dajemy wielokrotny strumein
                        this.inputStream = new SequenceInputStream(this.inputStream, part.getInputStream());
                    } else {
                        this.inputStream = part.getInputStream();
                    }
                }
            }
            store.close();
        } catch (Exception e) {
            if (store != null) {
                store.close();
            }
            return ERROR;
        }
        return SUCCESS;
    }

    public String delImage() throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store = null;
        try {
            store = session.getStore("pop3");
            Folder folder = getFolder(store);
            folder.open(Folder.READ_WRITE);
            Map parameters = ActionContext.getContext().getParameters();
            String[] id = (String[]) parameters.get("id");
            String[] id2 = (String[]) parameters.get("img_name");
            String image_name = "image_" + id[0] + "_" + id2[0];
            SubjectTerm term = new SubjectTerm(image_name);
            Message[] msgs = folder.search(term);
            folder.setFlags(msgs, new Flags(Flag.DELETED), true);
            folder.close(true);
            store.close();
            this.gallery = new Gallery();
            this.gallery.setTitle(id[0]);
        } catch (Exception e) {
            if (store != null) {
                store.close();
            }
            return ERROR;
        }
        return SUCCESS;
    }

    public String addImage() throws FileNotFoundException, IOException {
        Map parameters = ActionContext.getContext().getParameters();
        String[] id = (String[]) parameters.get("id");
        Object images = parameters.get("image");
        if (images != null && images instanceof File[]) {
            String[] filenames = (String[]) parameters.get("imageFileName");
            String ext = filenames[0].substring(filenames[0].length() - 3, filenames[0].length());
            if (ext.equalsIgnoreCase("zip")) {
                byte[] buf = new byte[1024];
                ZipInputStream zipinputstream = null;
                ZipEntry zipentry;
                File img = (File) ((File[]) images)[0];
                zipinputstream = new ZipInputStream(new FileInputStream(img));

                zipentry = zipinputstream.getNextEntry();
                while (zipentry != null) {
                    //for each entry to be extracted
                    String entryName = zipentry.getName();
                    System.out.println("entryname " + entryName);
                    int n;
                    FileOutputStream fileoutputstream;
                    File newFile = File.createTempFile("zip_file", null);
                    fileoutputstream = new FileOutputStream(newFile);
                    while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                        fileoutputstream.write(buf, 0, n);
                    }

                    fileoutputstream.close();
                    zipinputstream.closeEntry();
                    try {
                        sendImage(id[0], entryName, newFile);
                    } catch (Exception e) {
                        return ERROR;
                    }
                    zipentry = zipinputstream.getNextEntry();
                }//while

                zipinputstream.close();
                this.gallery = new Gallery();
                this.gallery.setTitle(id[0]);
                return SUCCESS;

            } else {
                File img = (File) ((File[]) images)[0];
                try {
                    sendImage(id[0], filenames[0], img);
                    this.gallery = new Gallery();
                    this.gallery.setTitle(id[0]);
                    return SUCCESS;
                } catch (Exception e) {
                    return ERROR;
                }
            }

        }
        this.gallery = new Gallery();
        this.gallery.setTitle(id[0]);
        return INPUT;
    }

    public String doInput() {
        return INPUT;
    }

    public String getError_mesgs() {
        return error_mesgs;
    }

    public void setError_mesgs(String error_mesgs) {
        this.error_mesgs = error_mesgs;
    }

    /**
     * @return Returns the employee.
     */
    public Message getMessage() {
        return message;
    }

    public List getGalleries() {
        return galleries;
    }

    public void setGalleries(List galleries) {
        this.galleries = galleries;
    }

    /**
     * @param employee
     *            The employee to set.
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * @return Returns the employees.
     */
    public List getMessages() {
        return messages;
    }

    public void setId(String id) {
        return;
    }

    String getId() {
        return this.gallery.getTitle();
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public LinkedList getImages() {
        return images;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream stream) {
        this.inputStream = stream;
    }

    public void setImages(LinkedList images) {
        this.images = images;
    }

    private void closeStore() {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("pop3");
            store.close();
        } catch (Exception ex) {
            Logger.getLogger(LibList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveGallery(Gallery gallery) {
        return;
    }

    private Folder getFolder(Store store) throws NoSuchProviderException, MessagingException {
        Map hsession = ActionContext.getContext().getSession();
        String pop3_addr = (String) hsession.get("addres");
        String login = (String) hsession.get("login");
        String pass = (String) hsession.get("pass");
        int max_att_size;
        try {
            max_att_size = (Integer) hsession.get("attr_size");
        } catch (Exception e) {
            max_att_size = 50;
        }

        Properties props = new Properties();
//        store.connect("pop3.wp.pl", "javaee1", "javaee2");
        store.connect(pop3_addr, login, pass);
        Folder folder = store.getFolder("INBOX");
        return folder;
    }

    public void sendMessage(Message mesg) throws MessagingException {
        Map hsession = ActionContext.getContext().getSession();
        String smtp_addr = (String) hsession.get("smtp_address");
        String email_addr = (String) hsession.get("email_address");
        String login = (String) hsession.get("login");
        String pass = (String) hsession.get("pass");

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store;

        Folder folder;

        mesg.setFrom(new InternetAddress(email_addr));
        mesg.addRecipient(Message.RecipientType.TO, new InternetAddress(email_addr));
        Transport transport = session.getTransport("smtp");
        transport.connect(smtp_addr, login, pass);
        transport.sendMessage(mesg, mesg.getAllRecipients());
        transport.close();
    }

    private void sendImage(String id, String name, File img) throws MessagingException, IOException {
        Map hsession = ActionContext.getContext().getSession();
        String login = (String) hsession.get("login");
        String pass = (String) hsession.get("pass");
        String smtp_addr = (String) hsession.get("smtp_address");
        String email_addr = (String) hsession.get("email_address");
        int max_att_size;
        try {
            max_att_size = (Integer) hsession.get("attr_size");
        } catch (Exception e) {
            max_att_size = 50;
        }

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store;

        Folder folder;

        // attach the file to the message
        FileInputStream fis = new FileInputStream(img);
        FileOutputStream fos;
        File output = File.createTempFile("mail", null);
        int size = 1024 * max_att_size;
        byte buffer[] = new byte[size];

        int count = 0;
        Transport transport = session.getTransport("smtp");
        transport.connect(smtp_addr, login, pass);
//        transport.connect("smtp.wp.pl", "javaee1", "javaee2");
        while (true) { /* czytanie porcji pliku i wysylanie */
            int i = fis.read(buffer, 0, size);
            if (i == -1) {
                break;
            }
            fos = new FileOutputStream(output);
            fos.write(buffer, 0, i);
            fos.flush();
            fos.close();
            /* wysylanie porcji file'a */
            MimeMessage mesg = new MimeMessage(session);
            mesg.setFrom(new InternetAddress(email_addr));
            if (i < size) {
                mesg.setSubject("image_" + id + "_" + name + "_last");
            } else {
                mesg.setSubject("image_" + id + "_" + name + "_part" + count);
            }
            mesg.setSentDate(new Date());
            mesg.addRecipient(Message.RecipientType.TO, new InternetAddress(email_addr));// create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();
            mbp2.attachFile(output);
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp2);
            mesg.setContent(mp);
            transport.sendMessage(mesg, mesg.getAllRecipients());
            ++count;
        }
        transport.close();
    }
}
