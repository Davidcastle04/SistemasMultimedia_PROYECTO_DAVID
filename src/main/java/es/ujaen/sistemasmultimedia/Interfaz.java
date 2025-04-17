/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package es.ujaen.sistemasmultimedia;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;


import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.File;

/**
 *
 * @author david
 */
public class Interfaz extends javax.swing.JFrame {

    JTextField ArchivoAbierto = new JTextField();
    boolean abiertomusica=false;
    Musica musica = null;

    public Interfaz() {
        initComponents();
        new DropTarget(jPanel1, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {}

            @Override
            public void dragOver(DropTargetDragEvent dtde) {}

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {}

            @Override
            public void dragExit(DropTargetEvent dte) {}

            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    // Aceptar el arrastre
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);

                    // Obtener los archivos arrastrados
                    Transferable transferable = dtde.getTransferable();
                    DataFlavor[] flavors = transferable.getTransferDataFlavors();

                    for (DataFlavor flavor : flavors) {
                        if (flavor.isFlavorJavaFileListType()) {
                            java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(flavor);
                            for (File file : files) {
                                String filePath = file.getAbsolutePath().toLowerCase();

                                // Copiar al portapapeles
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                                        new StringSelection(filePath), null
                                );

                                // Determinar el tipo por la extensión
                                if (filePath.endsWith(".mp3") || filePath.endsWith(".wav") || filePath.endsWith(".flac")) {
                                    if(!abiertomusica) {
                                        abiertomusica = true;
                                        musica = new Musica();
                                        jTabbedPane1.addTab("Musica", musica.Musica(file,ArchivoAbierto));
                                    }else{
                                        if(musica!=null){
                                            musica.addMusica(file);
                                        }else{
                                            musica = new Musica();
                                            jTabbedPane1.addTab("Musica", musica.Musica(file,ArchivoAbierto));
                                        }
                                    }
                                } else if (filePath.endsWith(".mp4") || filePath.endsWith(".avi") || filePath.endsWith(".mkv")) {
                                    //jTabbedPane1.addTab("Video "+file.getName(),VIDEO(file,ArchivoAbierto));
                                } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png") || filePath.endsWith(".gif")) {
                                    //jTabbedPane1.addTab("Imagen "+file.getName(),IMAGEN(file,ArchivoAbierto));
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Archivo no soportado: " + file.getName(),
                                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    private void initComponents() {

        JSeparator jSeparator1 = new JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        Label label3 = new Label();
        Button examinarArchivoBoton = new Button();

        Panel panel1 = new Panel();
        JPanel jPanel3 = new JPanel();
        Panel panel2 = new Panel();
        JSlider jSlider2 = new JSlider();
        Label label17 = new Label();
        Button button8 = new Button();
        Button button9 = new Button();
        Button button10 = new Button();
        JSlider jSlider3 = new JSlider();
        JLabel jLabel1 = new JLabel();
        JButton jButton4 = new JButton();
        JButton jButton5 = new JButton();
        Label label18 = new Label();
        Label label19 = new Label();
        Label label20 = new Label();
        Label label21 = new Label();
        Label label22 = new Label();
        Label label23 = new Label();
        Label label24 = new Label();
        Label label25 = new Label();
        JPanel jPanel4 = new JPanel();
        JPanel jPanel5 = new JPanel();
        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();
        JButton jButton3 = new JButton();
        Label label11 = new Label();
        Label label12 = new Label();
        Label label13 = new Label();
        Label label14 = new Label();
        Label label15 = new Label();
        Label label16 = new Label();

        Label label1 = new Label();
        JMenuBar jMenuBar1 = new JMenuBar();
        JMenu jMenu1 = new JMenu();
        JMenu jMenu5 = new JMenu();
        JMenu jMenu6 = new JMenu();
        JMenu jMenu7 = new JMenu();
        JMenu jMenu8 = new JMenu();
        JMenu jMenu9 = new JMenu();
        JMenu jMenu13 = new JMenu();
        JMenu jMenu14 = new JMenu();
        JMenu jMenu17 = new JMenu();
        JMenu jMenu2 = new JMenu();
        JMenu jMenu10 = new JMenu();
        JMenu jMenu11 = new JMenu();
        JMenu jMenu12 = new JMenu();
        JMenu jMenu15 = new JMenu();
        JMenu jMenu16 = new JMenu();
        JMenu jMenu4 = new JMenu();
        JMenu jMenu3 = new JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MultiStudio");
        setIconImage(new ImageIcon("../imagenes/logo.jpg").getImage());
        setBackground(new java.awt.Color(153, 153, 153));
        setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));



        jTabbedPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        label3.setText("Arrastre un archivo hacia está ventana o bien pulse en examinar");
        examinarArchivoBoton.setLabel("Examinar Archivos");
        examinarArchivoBoton.addActionListener(evt -> {
            JFileChooser selector = new JFileChooser();
            selector.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Filtro multimedia: audio, video e imagen
            javax.swing.filechooser.FileNameExtensionFilter filtro = new javax.swing.filechooser.FileNameExtensionFilter(
                    "Archivos multimedia (Audio, Video, Imagen)",
                    "mp3", "wav", "flac",     // Audio
                    "mp4", "avi", "mkv",      // Video
                    "jpg", "jpeg", "png", "gif" // Imágenes
            );
            selector.setFileFilter(filtro);

            int resultado = selector.showOpenDialog(Interfaz.this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = selector.getSelectedFile();
                System.out.println("Archivo seleccionado: " + archivoSeleccionado.getAbsolutePath());

                // Aquí podrías agregar lógica según el tipo de archivo
                String nombre = archivoSeleccionado.getName().toLowerCase();

                if (nombre.endsWith(".mp3") || nombre.endsWith(".wav") || nombre.endsWith(".flac")) {
                    if(!abiertomusica) {
                        abiertomusica = true;
                        musica = new Musica();
                        jTabbedPane1.addTab("Musica", musica.Musica(archivoSeleccionado,ArchivoAbierto));
                    }else{
                        if(musica!=null){
                            musica.addMusica(archivoSeleccionado);
                        }else{
                            musica = new Musica();
                            jTabbedPane1.addTab("Musica", musica.Musica(archivoSeleccionado,ArchivoAbierto));
                        }
                    }
                } else if (nombre.endsWith(".mp4") || nombre.endsWith(".avi") || nombre.endsWith(".mkv")) {
                    jTabbedPane1.addTab("Video "+archivoSeleccionado.getName(), VIDEO(archivoSeleccionado));
                } else if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png") || nombre.endsWith(".gif")) {
                    jTabbedPane1.addTab("Imagen "+archivoSeleccionado.getName(), IMAGEN(archivoSeleccionado));
                }
            }
        });


        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(206, 206, 206)
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(323, 323, 323)
                        .addComponent(examinarArchivoBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(219, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(186, 186, 186)
                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(examinarArchivoBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(142, Short.MAX_VALUE))
        );

        ImageIcon imagenAdd = new ImageIcon("../MultiStudio/imagenes/add.png");
        Image imagenEscalada = imagenAdd.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon imagenEscaladaAdd = new ImageIcon(imagenEscalada);
        jTabbedPane1.addTab("",imagenEscaladaAdd, jPanel1); // NOI18N

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 135, Short.MAX_VALUE)
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 135, Short.MAX_VALUE)
        );


        label17.setText("--:-- / --:--");

        button8.setLabel("button8");

        button9.setLabel("button9");

        button10.setLabel("button10");

        jLabel1.setText("Volumen");

        jButton4.setForeground(new java.awt.Color(255, 0, 102));
        jButton4.setText("Eliminar Video");

        jButton5.setText("Añadir a Videos Favoritos");

        label18.setText("Metadatos");

        label19.setText("Nombre del archivo ");

        label20.setText("Tamaño ");

        label21.setText("Duración");

        label22.setText("Formato ");

        label23.setText("Fecha de creación/modificación");

        label24.setText("Bitrate total");

        label25.setText("Titulo");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(button8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(button10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(label23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(label25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Vídeo", jPanel3);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 484, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
        );

        jButton1.setForeground(new java.awt.Color(255, 0, 51));
        jButton1.setText("Borrar Fotografia");

        jButton2.setText("Añadir a un album");

        jButton3.setText("Ver más fotos del mismo albúm");

        label11.setText("Metadatos de la imagen");

        label12.setText("Fecha y Hora");

        label13.setText("Modelo de cámara ");

        label14.setText("Tamaño ");

        label15.setText("ISO");

        label16.setText("Velocidad de obturación ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(20, 20, 20)
                        .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Imagen", jPanel4);

        ArchivoAbierto.setBackground(new java.awt.Color(153, 153, 255));
        ArchivoAbierto.setForeground(new java.awt.Color(255, 255, 255));
        ArchivoAbierto.setText("TIPO DE FICHERO ABIERTO");
        ArchivoAbierto.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        ArchivoAbierto.setOpaque(true);
        ArchivoAbierto.addActionListener(this::jTextField1ActionPerformed);

        label1.setFont(new java.awt.Font("DejaVu Sans Condensed", 2, 36)); // NOI18N
        label1.setText("MultiStudio");

        jMenu1.setText("Archivos");

        jMenu5.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/guardar.png")); // NOI18N
        jMenu5.setText("Guardar Fichero");

        jMenu6.setText("Archivo Original SIN MODIFICAR");
        jMenu5.add(jMenu6);

        jMenu7.setText("Guardar Modificaciones");
        jMenu5.add(jMenu7);

        jMenu1.add(jMenu5);

        jMenu8.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/open.png")); // NOI18N
        jMenu8.setText("Abrir Fichero");
        jMenu1.add(jMenu8);

        jMenu9.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/historial.png")); // NOI18N
        jMenu9.setText("Archivos Recientes");
        jMenu1.add(jMenu9);

        jMenu13.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/playlist.png")); // NOI18N
        jMenu13.setText("Cargar Playlist");
        jMenu1.add(jMenu13);

        jMenu14.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/album.png")); // NOI18N
        jMenu14.setText("Cargar Album Fotos");
        jMenu1.add(jMenu14);

        jMenu17.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/favorito.png")); // NOI18N
        jMenu17.setText("Añadir a Favoritos");
        jMenu1.add(jMenu17);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Editar");

        jMenu10.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/fichero.png")); // NOI18N
        jMenu10.setText("Cambiar Nombre Fichero");
        jMenu2.add(jMenu10);

        jMenu11.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/metadatos.png")); // NOI18N
        jMenu11.setText("Modificar Metadatos");
        jMenu2.add(jMenu11);

        jMenu12.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/caratula.png")); // NOI18N
        jMenu12.setText("Añadir Caratula");
        jMenu2.add(jMenu12);

        jMenu15.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/playlist.png")); // NOI18N
        jMenu15.setText("Crear Nueva PlayList");
        jMenu2.add(jMenu15);

        jMenu16.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/album.png")); // NOI18N
        jMenu16.setText("Crear Album de Fotos");
        jMenu2.add(jMenu16);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Ayuda");

        jMenu3.setIcon(new javax.swing.ImageIcon("/home/david/IdeaProjects/MultiStudio/imagenes/ayuda.png")); // NOI18N
        jMenu3.setText("Abrir Readme Ayuda");
        jMenu4.add(jMenu3);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ArchivoAbierto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ArchivoAbierto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed


    private JPanel VIDEO(File archivoSeleccionado) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    private JPanel IMAGEN(File archivoSeleccionado) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private javax.swing.JPanel jPanel1;

    private javax.swing.JTabbedPane jTabbedPane1;

    // End of variables declaration//GEN-END:variables
}