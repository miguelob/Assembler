package ensamblador.ui;

import ensamblador.languages.Languages;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.border.*;
import javax.swing.JOptionPane;


import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Cursor;

import java.lang.Runtime;
import java.net.URL;
public class JEnsamblador extends JFrame
{
    private Languages lg;
    private JLabel lblLanguage;
    private JButton btnSpanish;
    private JButton btnEnglish;
    private JLabel lblSubtitles;
    private JLabel logoApp;

    private URL urlLogo = this.getClass().getResource("/ensamblador/images/Source/logo.png");
    private URL urlIcono = this.getClass().getResource("/ensamblador/images/Source/logoApp.png");
    private String subtlt = "ICAI-RiSC-16    Assembler                                                        ESC to exit";
    
    public static void main(String args[]){
        new JEnsamblador();
    }
    public JEnsamblador(){
        this.iniComponentes();
        this.iniFrame();
    }
    private void iniComponentes(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }
        logoApp = new JLabel(new ImageIcon(urlIcono));
        lblLanguage = new JLabel("Language: ");
        btnEnglish = new JButton("English");
        btnSpanish = new JButton("Español");
        lblSubtitles = new JLabel(subtlt);

        btnSpanish.setBackground(new Color(220,220,220));
        btnEnglish.setBackground(new Color(220,220,220));
        btnEnglish.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSpanish.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
        this.getRootPane().setBorder(new MatteBorder(3,3,3,3, Color.lightGray));

        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
        north.add(lblLanguage);
        north.setBackground(Color.WHITE);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        center.add(btnEnglish);
        center.add(btnSpanish);
        center.setBackground(Color.WHITE);

        JPanel end = new JPanel(new FlowLayout(FlowLayout.CENTER));
        end.add(lblSubtitles);
        end.setBackground(Color.WHITE);

        JPanel jpLogo = new JPanel();
        jpLogo.add(logoApp);
        jpLogo.setBackground(Color.WHITE);

        this.add(north,BorderLayout.NORTH);
        this.add(center,BorderLayout.CENTER);
        this.add(end,BorderLayout.PAGE_END);
        this.add(jpLogo,BorderLayout.NORTH);

        this.setUndecorated(true);

        btnEnglish.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){   
                try{
                    lg = new Languages(Languages.ENGLISH);
                    JEnsamblador.this.setVisible(false);
                    JEnsamblador.this.dispose();
                    new ensamblador.editor.Editor(lg);
                }catch(FileNotFoundException fnfe){
                    JOptionPane.showMessageDialog(JEnsamblador.this,"Could not find English.txt. Restore the file in order to continue.","Error",JOptionPane.ERROR_MESSAGE);
                }catch(IOException ioe){
                    JOptionPane.showMessageDialog(JEnsamblador.this,"There was an error while loading the language. Please, restart the app","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnSpanish.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    lg = new Languages(Languages.SPANISH);
                    JEnsamblador.this.setVisible(false);
                    JEnsamblador.this.dispose();
                    new ensamblador.editor.Editor(lg);
                }catch(FileNotFoundException fnfe){
                    JOptionPane.showMessageDialog(JEnsamblador.this,"No se ha podido encontrar Spanish.txt. Restaure el archivo para continuar.","Error",JOptionPane.ERROR_MESSAGE);
                }catch(IOException ioe){
                    JOptionPane.showMessageDialog(JEnsamblador.this,"Error al cargar el idioma. Por favor, reinicie el programa.","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnSpanish.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                    btnSpanish.doClick();
                else if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
                    JEnsamblador.this.finPrograma();
            }
        });
        btnEnglish.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                    btnEnglish.doClick();
                else if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
                    JEnsamblador.this.finPrograma();
            }
        });
    }
    private void iniFrame(){
        this.setTitle("Menu");
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(urlLogo).getImage());
        this.setVisible(true);
    }
    private void finPrograma(){
        
        this.setVisible(false);
        this.dispose();
        System.exit(0);
    }
}
