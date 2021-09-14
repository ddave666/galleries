/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Dave
 */
public class Login extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        try {
        String pop3_addr = request.getParameter("pop_address");
        String login = request.getParameter("login_name");
        String pass = request.getParameter("pass");
        String smtp_addr = request.getParameter("smtp_address");
        String email_addr = request.getParameter("email_address");
        int max_att_size;
        try {
            max_att_size = Integer.parseInt(request.getParameter("att_size"));
        } catch (Exception e) {
            max_att_size = 50;
        }
        ServletContext sc = this.getServletContext();
        if (pop3_addr == null || login == null || pass == null || smtp_addr == null || email_addr == null) {
            request.setAttribute("error_message", "Podaj wszystkie dane logowania\n");
            RequestDispatcher rd = sc.getRequestDispatcher("/login_form.jsp");
            if (rd != null) {
                try {
                    rd.forward(request, response);
                } catch (Exception e) {
                    sc.log("Problem invoking JSP.", e);
                }
            }
            return;
        }// Create empty properties
        Properties props = new Properties();

// Get session
        Session session = Session.getDefaultInstance(props, null);

// Get the store
        Store store;
        try {
            store = session.getStore("pop3");
                store.connect(pop3_addr, login, pass);

// Get folder
//            Folder folder = store.getFolder("INBOX");
//            folder.open(Folder.READ_ONLY);
            store.close();
            HttpSession hsession = request.getSession();
            hsession.setAttribute("addres", pop3_addr);
            hsession.setAttribute("login", login);
            hsession.setAttribute("pass", pass);
            hsession.setAttribute("attr_size", max_att_size);
            hsession.setAttribute("smtp_address", smtp_addr);
            hsession.setAttribute("email_address", email_addr);
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+ "/ListGalleries.action"));
//            RequestDispatcher rd = sc.getRequestDispatcher("/libList.jsp");
//            if (rd != null) {
//                try {
//                    rd.forward(request, response);
//                } catch (Exception e) {
//                    sc.log("Problem invoking JSP.", e);
//                }
//            }

// Get directory
//            Message message[] = folder.getMessages();
//
//            for (int i = 0, n = message.length; i < n; i++) {
//                System.out.println(i + ": " + message[i].getFrom()[0] + "\t" + message[i].getSubject());
//            }

// Close connection
//            folder.close(false);
//            store.close();

        } catch (Exception ex) {
            request.setAttribute("error_message", "Niepoprawne dane konta");
            RequestDispatcher rd = sc.getRequestDispatcher("/login_form.jsp");
            if (rd != null) {
                try {
                    rd.forward(request, response);
                } catch (Exception e) {
                    sc.log("Problem invoking JSP.", e);
                }
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
