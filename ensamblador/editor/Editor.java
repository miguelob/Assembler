/** Paquete con la clase principal */
package ensamblador.editor;

import ensamblador.cmdcode.Compilador;
import ensamblador.languages.Languages;
import ensamblador.io.Archivos;
import ensamblador.images.Images;

import java.util.HashMap;
import java.util.Collection;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import javax.swing.text.Document;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.net.URL;

import java.io.IOException;
import java.nio.file.Files;
import java.io.FileWriter;
import java.io.*;

import java.nio.file.Paths;

/**
 * Clase principal del programa.
 * Esta clase es la encargada de comunicar las distintas clases e implementar sus funcionalidades.
 * Tambien es la encargada de gestionar todas las exceptiones del programa.
 * 
 * @version Se especifica en los ficheros de idiomas
 * @author Miguel Oleo Blanco
 */

public class Editor extends JFrame {
    private Languages language;
    private String idioma;
    /** Es el HashMap el cual se rellenara con el idioma (Escogido y cargado en JEnsamblador).*/
    private HashMap<String, String> hmIdioma = null;
    private JButton btnCompile;
    private JButton btnGuardar;
    private JLabel lblTitle;
    private JLabel lbl1;
    private JLabel lbl2;
    private JButton btnLoad;
    private ImageIcon logo = new ImageIcon(this.getClass().getResource("/ensamblador/images/Source/logo.png"));
    private ImageIcon saveFalse = new ImageIcon(this.getClass().getResource("/ensamblador/images/Source/save_false.png"));
    private ImageIcon saveTrue = new ImageIcon(this.getClass().getResource("/ensamblador/images/Source/save_true.png"));
    private ImageIcon compilaMAL = new ImageIcon(this.getClass().getResource("/ensamblador/images/Source/compilaMAL.png"));
    private ImageIcon compilaOK = new ImageIcon(this.getClass().getResource("/ensamblador/images/Source/compilaOK.png"));
    private ImageIcon loading = new ImageIcon(this.getClass().getResource("/ensamblador/images/Source/loading.gif"));
    /** Es el atributo principal (un JTextArea). */
    private JTextArea txtaTexto;
    private JScrollPane jscroll;
    private JMenuBar jmenuBar;
    private JMenu jmenu1;
    private JMenu jmenu2;
    private JMenuItem jGuardar;
    private JMenuItem jGuardarComo;
    private JMenuItem jTeoria;
    private JMenuItem jSoporte;
    private JMenuItem jTeclas;
    /** Contiene la ruta del archivo con el que se trabaja, lo guardo para simplificar procesos. */
    private String ruta = null;
    /** Indica si el texto mostrado es la ultima version guardada. */
    private boolean modificado = false;
    /** Indica si hay que borrar la carpeta temporal al salir del programa. */
    private boolean borrarCarpeta = false;
    private JLabel lblInfo;
    private int tamanio = 13;
    /** Clase que nos proporciona Java, que nos implementa la funcionalidad Undo/Redo. */
    protected UndoManager undoManager;

    /**
     * Reescala las imagenes, carga el HashMap e inicia componentes y ventana.
     * @param language es la referencia a la clase Language, la cual contiene el hashmap con el texto del idioma.
     */
    public Editor(Languages language) {
        this.resizeImages();
        this.language = language;
        this.iniLanguage();
        this.iniComponents();
        this.iniFrame();
    }
    /**
     * Inicializacion de todos los componentes graficos necesarios para la GUI.
     * Tambien incluye los distintos actionListener de los componentes y de algunas funcionalidades extra.
     */
    private void iniComponents() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }
        lblInfo = new JLabel("");
        undoManager = new UndoManager();
        lblTitle = new JLabel(hmIdioma.get("Titulo1").toString());
        lbl1 = new JLabel(hmIdioma.get("Titulo2").toString());
        lbl2 = new JLabel(hmIdioma.get("Titulo3").toString());
        btnLoad = new JButton(hmIdioma.get("Cargar").toString());
        btnCompile = new JButton(hmIdioma.get("Compilar").toString());
        btnGuardar = new JButton(hmIdioma.get("Guardar").toString());
        txtaTexto = new JTextArea(30, 60);
        jscroll = new JScrollPane(txtaTexto);
        jscroll.setBounds(10, 50, 400, 300);

        jmenuBar = new JMenuBar();
        jmenu1 = new JMenu(hmIdioma.get("Extra").toString());
        jTeoria = new JMenuItem(hmIdioma.get("Tutorial").toString());
        jSoporte = new JMenuItem(hmIdioma.get("Soporte").toString());
        jTeclas = new JMenuItem(hmIdioma.get("Teclas rapidas").toString());

        jmenu1.add(jTeoria);
        jmenu1.add(jSoporte);
        jmenu1.add(jTeclas);

        jmenu2 = new JMenu(hmIdioma.get("Archivo").toString());
        jGuardar = new JMenuItem(hmIdioma.get("Guardar").toString());
        jGuardarComo = new JMenuItem(hmIdioma.get("Guardar como").toString());
        jmenu1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jmenu2.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLoad.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCompile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLoad.setBackground(new Color(220,220,220));
        btnCompile.setBackground(new Color(220,220,220));
        btnGuardar.setBackground(new Color(220,220,220));
        jTeoria.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jSoporte.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jTeclas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jGuardarComo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jmenu2.add(jGuardar);
        jmenu2.add(jGuardarComo);

        jmenuBar.add(jmenu2);
        jmenuBar.add(jmenu1);

        this.setJMenuBar(jmenuBar);

        this.setLayout(new BorderLayout());

        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.add(lblTitle);
        north.add(lbl1);
        north.add(lbl2);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.add(btnLoad);
        south.add(btnGuardar);
        south.add(btnCompile);
        south.add(lblInfo);

        this.add(north, BorderLayout.PAGE_START);
        this.add(jscroll, BorderLayout.CENTER);
        this.add(south, BorderLayout.PAGE_END);

        txtaTexto.setFont(new Font("Arial", Font.LAYOUT_LEFT_TO_RIGHT, tamanio));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (modificado) {
                    Object[] opciones = { hmIdioma.get("Guardar y salir").toString(),
                            hmIdioma.get("Salir").toString(), };
                    int guardar = JOptionPane.showOptionDialog(Editor.this,
                            hmIdioma.get("Perdera contenido").toString(), hmIdioma.get("Aviso").toString(),
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
                    if (guardar == JOptionPane.YES_OPTION) {
                        Editor.this.guardar();
                        Editor.this.dispose();
                        System.exit(0);
                    }
                }
                if (borrarCarpeta){
                    try{
                        Compilador.borrarDirectorio();
                    }catch(IOException ioe){
                        JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error borrar").toString(),hmIdioma.get("Error").toString(),JOptionPane.WARNING_MESSAGE);
                        ioe.printStackTrace();
                    }catch(InterruptedException ie){
                        JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error wait").toString(),hmIdioma.get("Error").toString(),JOptionPane.WARNING_MESSAGE);
                        ie.printStackTrace();
                    }
                }
                Editor.this.dispose();
                System.exit(0);
            }
        });
        txtaTexto.getDocument().addUndoableEditListener(undoManager);
        btnGuardar.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Editor.this.guardar();
            }
        });
        btnLoad.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser  j = new JFileChooser();
                j.setCurrentDirectory(new File("."));
                int rtn = j.showOpenDialog(Editor.this);
                if(rtn == JFileChooser.APPROVE_OPTION)
                {
                    ruta = j.getSelectedFile().getPath();
                    
                    String extension = ruta.substring(ruta.length()-3,ruta.length());
                    if(extension.equals("asm") || extension.equals("txt"))
                    {
                        try{
                            if(modificado)
                            {   
                                Object[] opciones = {hmIdioma.get("Guardar").toString(),hmIdioma.get("Cancelar").toString()};
                                int guardar = JOptionPane.showOptionDialog(Editor.this,hmIdioma.get("Perdera contenido").toString()
                                    ,hmIdioma.get("Aviso").toString(),JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,opciones,opciones[0]);
                                if(guardar == JOptionPane.YES_OPTION)
                                {
                                    Editor.this.guardar();
                                }
                            }
                            txtaTexto.setText(Archivos.cargar(ruta));
                            modificado = false;
                            lblInfo.setIcon(null);
                        }catch(FileNotFoundException fnfe){
                            JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error cargar").toString(),hmIdioma.get("Error").toString(),JOptionPane.WARNING_MESSAGE);
                            fnfe.printStackTrace();
                        }catch(IOException ioe){
                            JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error lectura").toString(),hmIdioma.get("Error").toString(),JOptionPane.WARNING_MESSAGE);
                            ioe.printStackTrace();
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error extension").toString(),hmIdioma.get("Error").toString(),JOptionPane.WARNING_MESSAGE);              
                }   
            }
        });
        btnCompile.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e) {
                Editor.this.compilar();
            }
        });
        txtaTexto.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getModifiersEx()!=KeyEvent.CTRL_DOWN_MASK && !modificado)
                {
                    modificado = true;
                    lblInfo.setIcon(saveFalse);
                }
            }
                    @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode())
                {
                    case KeyEvent.VK_S:
                        if(e.getModifiersEx()==KeyEvent.CTRL_DOWN_MASK)
                            Editor.this.guardar();
                        break;
                    case KeyEvent.VK_Q:
                        if(e.getModifiersEx()==KeyEvent.CTRL_DOWN_MASK){
                            Editor.this.guardarComo();
                        }
                        break;
                    case KeyEvent.VK_E:
                        if(e.getModifiersEx()==KeyEvent.CTRL_DOWN_MASK)
                            Editor.this.compilar();
                        break;
                    case KeyEvent.VK_PLUS:
                        if(e.getModifiersEx()==KeyEvent.CTRL_DOWN_MASK){
                            tamanio++;
                            txtaTexto.setFont(new Font("Arial", Font.LAYOUT_LEFT_TO_RIGHT, tamanio));
                        }
                        break;
                    case KeyEvent.VK_MINUS:
                        if(e.getModifiersEx()==KeyEvent.CTRL_DOWN_MASK){
                            tamanio--;
                            txtaTexto.setFont(new Font("Arial", Font.LAYOUT_LEFT_TO_RIGHT, tamanio));
                        }
                        break;
                    case KeyEvent.VK_Z:
                        if(e.getModifiersEx()==KeyEvent.CTRL_DOWN_MASK){
                            try{
                                if(undoManager.canUndo()){
                                    undoManager.undo();
                                    modificado = true;
                                    lblInfo.setIcon(saveFalse);
                                }
                            }catch(CannotUndoException cue){
                                JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error undo").toString(),hmIdioma.get("Error").toString(),JOptionPane.WARNING_MESSAGE);
                                cue.printStackTrace();
                            }
                        }
                        break;
                    case KeyEvent.VK_R:
                        if(e.getModifiersEx()==KeyEvent.CTRL_DOWN_MASK){
                            try{
                                if(undoManager.canRedo()){
                                    undoManager.redo();
                                    modificado = true;
                                    lblInfo.setIcon(saveFalse);
                                }
                            }catch(CannotRedoException cue){
                                JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error redo").toString(),hmIdioma.get("Error").toString(),JOptionPane.WARNING_MESSAGE);
                                cue.printStackTrace();
                            }
                        }
                        break;
                }
            }

        });
        jSoporte.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Contacto version").toString()+"\n"+hmIdioma.get("Contacto email").toString(),"Info",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        jGuardar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Editor.this.guardar();
            }
        });
        jGuardarComo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Editor.this.guardarComo();
            }
        });
        jTeoria.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                try {
                    String url = "https://sifo.comillas.edu/pluginfile.php/2361559/mod_resource/content/6/Programaci%C3%B3n%20ICAI-RiSC-16.pdf";
                    Desktop.getDesktop().browse(new URL(url).toURI());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error url").toString(),hmIdioma.get("Error").toString(),JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
        jTeclas.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                StringBuilder sb = new StringBuilder();
                    sb.append("Ctrl+S "+"\u21D2 ")
                        .append(hmIdioma.get("Guardar").toString()+"\n")
                        .append("Ctrl+Q "+"\u21D2 ")
                        .append(hmIdioma.get("Guardar como").toString()+"\n")
                        .append("Ctrl+E "+"\u21D2 ")
                        .append(hmIdioma.get("Compilar").toString()+"\n")
                        .append("Ctrl + "+"\u21D2 ")
                        .append(hmIdioma.get("Hacer zoom").toString()+"\n")
                        .append("Ctrl - "+"\u21D2 ")
                        .append(hmIdioma.get("Quitar zoom").toString()+"\n")
                        .append("Ctrl+Z "+"\u21D2 ")
                        .append(hmIdioma.get("Deshacer").toString()+"\n")
                        .append("Ctrl+R "+"\u21D2 ")
                        .append(hmIdioma.get("Rehacer").toString()+"\n");
                JOptionPane.showMessageDialog(Editor.this,sb.toString(),hmIdioma.get("Teclas rapidas").toString(),JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    /**
     * Inicializacion de los atributos de la ventana, a traves de los metodos que nos proporciona JFrame.
     * No aporta funcionalidad, si no diseño. Por ultimo, haze visible la ventana.
     */
    private void iniFrame()
    {
        this.setTitle("ICAI-RiSC-16 Bits");
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setIconImage(logo.getImage());
        this.setVisible(true);
    }
    /**
     * Carga el HasHmap con el idioma seleccionado en el menu.
     */
    private void iniLanguage()
    {
        hmIdioma = language.getLanguage();
    }
    /**
     * Gestiona la funcionalidad de Guardar, y a su vez llama a guardarComo en caso de que sea necesario.
     */
    public void guardar()
    {
        String contenido = txtaTexto.getText();
        if(ruta!=null)
        {
            try{
                Archivos.guardar(contenido,ruta);
                modificado = false;
                lblInfo.setIcon(saveTrue); 
            }catch(IOException ioe){
                JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error guardar").toString(),hmIdioma.get("Error").toString(),JOptionPane.ERROR_MESSAGE);
                ioe.printStackTrace();
            }
             
        }else{
            this.guardarComo();
        }
    }
    private void guardarComo()
    {
        String contenido = txtaTexto.getText();
        JFileChooser  j = new JFileChooser();
        j.setCurrentDirectory(new File("."));
        int rtn = j.showOpenDialog(this);
        if(rtn == JFileChooser.APPROVE_OPTION)
        {
            ruta = j.getSelectedFile().toString();
            try {
                if(ruta.substring(ruta.length()-3,ruta.length()).equals("txt") || ruta.substring(ruta.length()-3,ruta.length()).equals("asm"))
                {
                    Archivos.guardar(contenido, ruta);
                }else{
                    Archivos.guardar(contenido,ruta+".asm");
                }
                modificado = false;
                lblInfo.setIcon(saveTrue);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this,hmIdioma.get("Error guardar").toString(),hmIdioma.get("Error").toString(),JOptionPane.ERROR_MESSAGE);
                ioe.printStackTrace();
            }
        }
    }
    /**
     * Se encarga de gestionar todo lo necesario para compilar el archivo.
     * La clase Compilador solo compila, por lo que aquí es necesario hacer el resto de comprobaciones y gestiones.
     */
    private void compilar()
    {
        boolean fallo = false;
        if(ruta == null)
        {
            JOptionPane.showMessageDialog(this,hmIdioma.get("No archivo").toString(),hmIdioma.get("Error").toString(),JOptionPane.WARNING_MESSAGE);
        }else{
            if(modificado)
            {   
                Object[] opciones = {hmIdioma.get("Guardar").toString(),hmIdioma.get("Cancelar").toString()};
                int guardar = JOptionPane.showOptionDialog(this,hmIdioma.get("Perdera contenido").toString()
                    ,hmIdioma.get("Aviso").toString(),JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,opciones,opciones[0]);
                if(guardar == JOptionPane.YES_OPTION)
                {
                    Editor.this.guardar();
                }
            }
            String extension = ruta.substring(ruta.length()-3,ruta.length());
            if(extension.equals("asm"))
            {
                try{
                    Compilador.compilar(ruta);
                    String error = Archivos.errorCompilacion();
                    borrarCarpeta=true;
                    if(!error.equals(""))
                    {
                        JOptionPane.showMessageDialog(this, error,hmIdioma.get("Error").toString(),JOptionPane.ERROR_MESSAGE);
                        fallo = true;
                        lblInfo.setIcon(compilaMAL);
                    }
                }catch(IOException ioe){
                    JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error comp cmd").toString(),hmIdioma.get("Error").toString(),JOptionPane.ERROR_MESSAGE);
                    ioe.printStackTrace();
                }catch(InterruptedException ie){
                    JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error wait").toString(),hmIdioma.get("Error").toString(),JOptionPane.ERROR_MESSAGE);
                    ie.printStackTrace();
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,hmIdioma.get("Error comp general").toString(),hmIdioma.get("Error").toString(),JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                if(!fallo){
                    JFileChooser guardarSalida = new JFileChooser();
                    guardarSalida.setCurrentDirectory(new File("."));
                    guardarSalida.setDialogTitle(hmIdioma.get("Guardar compilador").toString());
                    guardarSalida.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int rtn = guardarSalida.showOpenDialog(this);
                    if(rtn == JFileChooser.APPROVE_OPTION){
                        try{
                            Compilador.cambiarDirectorio(guardarSalida.getSelectedFile().toString());
                            lblInfo.setIcon(compilaOK);
                        }catch(IOException ioe){
                            JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error comp cd").toString(),hmIdioma.get("Error").toString(),JOptionPane.ERROR_MESSAGE);
                            ioe.printStackTrace();
                        }
                        catch(InterruptedException ie){
                            JOptionPane.showMessageDialog(Editor.this,hmIdioma.get("Error wait").toString(),hmIdioma.get("Error").toString(),JOptionPane.ERROR_MESSAGE);
                            ie.printStackTrace();
                        }
                    }
                }
            }else
                JOptionPane.showMessageDialog(this,hmIdioma.get("Error extension comp").toString(),hmIdioma.get("Error").toString(),JOptionPane.WARNING_MESSAGE);
        }
    }
    /**
     * Metodo que se encarga de reescalar las imagenes para que quepan bien en el GUI.
     * Para llevar esto a cabo, llama a la clase Images.
     */
    private void resizeImages()
    {
        saveFalse = Images.resize(saveFalse,25,25);
        saveTrue = Images.resize(saveTrue,25,25);
        compilaMAL = Images.resize(compilaMAL,25,25);
        compilaOK = Images.resize(compilaOK,25,25);
        loading = Images.resize(loading,25,25);
    }
}
