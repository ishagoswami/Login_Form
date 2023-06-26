package login_form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class LoginForm extends JFrame
{
    //sets and checks which font to choose____ This is the fonts that would be used by different components
    final private Font mainFont = new Font("Segoe print", Font.BOLD, 18);
    JTextField tfEmail;
    JPasswordField pfPassword;

    public void initialize()
    {
        /******** FORM PANNEL ********/
        JLabel lbLoginForm = new JLabel("Login Form", SwingConstants.CENTER);   //label displays the text login form
        lbLoginForm.setFont(mainFont);   

        //create new label and set its font
        
        JLabel lbEmail = new JLabel("Email");    //displays Email
        lbEmail.setFont(mainFont);
        
        tfEmail = new JTextField();
        tfEmail.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);

        //following pannel will contain these different components using a grid layout ..... by default provides a border layout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0,1,10,10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(lbLoginForm);
        formPanel.add(lbEmail);
        formPanel.add(tfEmail);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);



        /******** BUTTONS PANNEL ********/
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(mainFont);
        btnLogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                User user = getAuthenticatedUser(email, password);

                if(user != null)
                {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.initialize(user);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(LoginForm.this,
                    "Email or Password Invaalid",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });

        JButton btnCancel = new JButton("CANCEL");
        btnCancel.setFont(mainFont);
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                dispose();
            }
            
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1,2,10,0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnCancel);
        

         
        /******** INITIALISE THE FRAME ********/
        add(formPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);


       




        setTitle("Login Form");  //title of the window
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);   //determines closing the window on clicking the close button
        setSize(400,500);   
        setMinimumSize(new Dimension(350, 450));
        // setResizable(false);
        setLocationRelativeTo(null);  //brings window to the middle of the screen
        setVisible(true);   //makes frame visible
    }

    private User getAuthenticatedUser(String email, String password)
    {
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //Connected to database successfully...

            String sql = "SELECT * FROM users WHERE email =? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
                
            }
            preparedStatement.close();
            conn.close();
        }
        catch(Exception e)
        {
            System.out.println("DATABASE CONNECTION FAILED!");
        }
        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        loginForm.initialize();
    }
}
