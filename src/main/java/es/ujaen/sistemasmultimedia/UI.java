package es.ujaen.sistemasmultimedia;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.File;

public class UI extends JFrame {

    JTextField ArchivoAbierto = new JTextField();
    private JPanel jPanel1 = new JPanel();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    boolean abiertomusica=false;
    Multimedia.Musica1 musica = null;
    JSeparator jSeparator1 = new JSeparator();
    Label label3 = new Label();
    Button examinarArchivoBoton = new Button();
    Panel panel1 = new Panel();
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
    GroupLayout panel1Layout = new GroupLayout(panel1);
    GroupLayout jPanel1Layout = new GroupLayout(jPanel1);



    public UI() {
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
                                    if (!abiertomusica) {
                                        abiertomusica = true;
                                        musica = new Multimedia.Musica1();
                                        Component contenido = musica.AUDIO(file, ArchivoAbierto);
                                        jTabbedPane1.addTab(null, contenido);
                                        jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                                new PestanaConCerrar(jTabbedPane1, "Musica", () -> {
                                                    abiertomusica = false;
                                                    actualizarMenus();
                                                }));
                                    } else {
                                        if (musica != null) {
                                            musica.addMusica(file);
                                        } else {
                                            musica = new Multimedia.Musica1();
                                            Component contenido = musica.AUDIO(file, ArchivoAbierto);
                                            jTabbedPane1.addTab(null, contenido);
                                            jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                                    new PestanaConCerrar(jTabbedPane1, "Musica", () -> {
                                                        abiertomusica = false;
                                                        actualizarMenus();
                                                    }));
                                        }
                                    }
                                } else if (filePath.endsWith(".mp4") || filePath.endsWith(".avi") || filePath.endsWith(".mkv")) {
                                    Component contenido = new Multimedia.Video1().getVideo();
                                    jTabbedPane1.addTab(null, contenido);
                                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                            new PestanaConCerrar(jTabbedPane1, "Video " + file.getName(), UI.this::actualizarMenus));
                                } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png") || filePath.endsWith(".gif")) {
                                    Component contenido = new Multimedia.Imagen1().img();
                                    jTabbedPane1.addTab(null, contenido);
                                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                            new PestanaConCerrar(jTabbedPane1, "Imagen "+file.getName(), UI.this::actualizarMenus));
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
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Aquí puedes comprobar si hay cambios sin guardar
                boolean hayCambiosSinGuardar = /* tu lógica aquí, por ejemplo: */ false;

                String mensaje = "¿Está seguro de que quiere cerrar la aplicación?";
                if (hayCambiosSinGuardar) {
                    mensaje += "\nLos cambios no se guardarán en caso de que se hayan modificado.";
                }

                int opcion = JOptionPane.showConfirmDialog(
                        UI.this,
                        mensaje,
                        "Confirmar cierre",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    // Cierra la aplicación
                    System.exit(0);
                }
                // Si elige NO, no hace nada y la ventana sigue abierta
            }
        });

    }

    private void initComponents() {


        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MultiStudio");
        setIconImage(new ImageIcon("../imagenes/logo.jpeg").getImage());
        setBackground(new java.awt.Color(153, 153, 153));
        setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));
        jTabbedPane1.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        label3.setText("Arrastre un archivo hacia está ventana o bien pulse en examinar");
        examinarArchivoBoton.setLabel("Examinar Archivos");
        examinarArchivoBoton.addActionListener(evt -> {
            JFileChooser selector = getJFileChooser();

            int resultado = selector.showOpenDialog(UI.this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = selector.getSelectedFile();
                System.out.println("Archivo seleccionado: " + archivoSeleccionado.getAbsolutePath());

                // Aquí podrías agregar lógica según el tipo de archivo
                String nombre = archivoSeleccionado.getName().toLowerCase();

                if (nombre.endsWith(".mp3") || nombre.endsWith(".wav") || nombre.endsWith(".flac")) {
                    if (!abiertomusica) {
                        abiertomusica = true;
                        musica = new Multimedia.Musica1();
                        Component contenido = musica.AUDIO(archivoSeleccionado, ArchivoAbierto);
                        jTabbedPane1.addTab(null, contenido);
                        jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                new PestanaConCerrar(jTabbedPane1, "Musica", () -> {
                                    abiertomusica = false;
                                    actualizarMenus();
                                }));
                    } else {
                        if (musica != null) {
                            musica.addMusica(archivoSeleccionado);
                        } else {
                            musica = new Multimedia.Musica1();
                            Component contenido = musica.AUDIO(archivoSeleccionado, ArchivoAbierto);
                            jTabbedPane1.addTab(null, contenido);
                            jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                    new PestanaConCerrar(jTabbedPane1, "Musica", () -> {
                                        abiertomusica = false;
                                        actualizarMenus();
                                    }));
                        }
                    }
                } else if (nombre.endsWith(".mp4") || nombre.endsWith(".avi") || nombre.endsWith(".mkv")) {
                    Component contenido = new Multimedia.Video1().getVideo();
                    jTabbedPane1.addTab(null, contenido);
                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                            new PestanaConCerrar(jTabbedPane1, "Video" + archivoSeleccionado.getName(), UI.this::actualizarMenus));
                } else if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png") || nombre.endsWith(".gif")) {
                    Component contenido = new Multimedia.Imagen1().img();
                    jTabbedPane1.addTab(null, contenido);
                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                            new PestanaConCerrar(jTabbedPane1, "Imagen "+archivoSeleccionado.getName(), UI.this::actualizarMenus));
                }
            }
        });



        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
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


        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 135, Short.MAX_VALUE));
        panel1Layout.setVerticalGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 135, Short.MAX_VALUE));



        ArchivoAbierto.setBackground(new java.awt.Color(153, 153, 255));
        ArchivoAbierto.setForeground(new java.awt.Color(255, 255, 255));
        ArchivoAbierto.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        ArchivoAbierto.setOpaque(true);
        label1.setFont(new java.awt.Font("DejaVu Sans Condensed", 2, 36));



        ArchivoAbierto.setText("TIPO DE FICHERO ABIERTO");
        label1.setText("MultiStudio");
        jMenu1.setText("Archivos");
        jMenu5.setText("Guardar Fichero");
        jMenu6.setText("Archivo Original SIN MODIFICAR");
        jMenu7.setText("Guardar Modificaciones");
        jMenu8.setText("Abrir Fichero");
        jMenu9.setText("Archivos Recientes");
        jMenu13.setText("Cargar Playlist");
        jMenu14.setText("Cargar Album Fotos");
        jMenu17.setText("Añadir a Favoritos");
        jMenu2.setText("Editar");
        jMenu10.setText("Cambiar Nombre Fichero");
        jMenu11.setText("Modificar Metadatos");
        jMenu12.setText("Añadir Caratula");
        jMenu15.setText("Crear Nueva PlayList");
        jMenu16.setText("Crear Album de Fotos");
        jMenu4.setText("Ayuda");
        jMenu3.setText("Abrir Readme Ayuda");


        jMenu5.setIcon(new ImageIcon("../MultiStudio/imagenes/guardar.png"));
        jMenu8.setIcon(new ImageIcon("../MultiStudio/imagenes/open.png"));
        jMenu9.setIcon(new ImageIcon("../MultiStudio/imagenes/historial.png"));
        jMenu13.setIcon(new ImageIcon("../MultiStudio/imagenes/playlist.png"));
        jMenu14.setIcon(new ImageIcon("../MultiStudio/imagenes/album.png"));
        jMenu17.setIcon(new ImageIcon("../MultiStudio/imagenes/favorito.png"));
        jMenu10.setIcon(new ImageIcon("../MultiStudio/imagenes/fichero.png"));
        jMenu11.setIcon(new ImageIcon("../MultiStudio/imagenes/metadatos.png"));
        jMenu12.setIcon(new ImageIcon("../MultiStudio/imagenes/caratula.png"));
        jMenu15.setIcon(new ImageIcon("../MultiStudio/imagenes/playlist.png"));
        jMenu16.setIcon(new ImageIcon("../MultiStudio/imagenes/album.png"));
        jMenu3.setIcon(new ImageIcon("../MultiStudio/imagenes/ayuda.png"));


        setJMenuBar(jMenuBar1);


        jMenu2.add(jMenu10);
        jMenuBar1.add(jMenu1);
        jMenu1.add(jMenu17);
        jMenu1.add(jMenu14);
        jMenu1.add(jMenu13);
        jMenu1.add(jMenu9);
        jMenu1.add(jMenu8);
        jMenu5.add(jMenu6);
        jMenu5.add(jMenu7);
        jMenu1.add(jMenu5);
        jMenu2.add(jMenu11);
        jMenu2.add(jMenu12);
        jMenu2.add(jMenu15);
        jMenu2.add(jMenu16);
        jMenuBar1.add(jMenu2);
        jMenu4.add(jMenu3);
        jMenuBar1.add(jMenu4);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator1)
                        .addGroup(layout.createSequentialGroup().addGap(102, 102, 102).addComponent(label1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(ArchivoAbierto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE).addGap(98, 98, 98))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(47, Short.MAX_VALUE).addComponent(jTabbedPane1, GroupLayout.PREFERRED_SIZE, 792, GroupLayout.PREFERRED_SIZE).addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(label1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(21, 21, 21))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(ArchivoAbierto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)))
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTabbedPane1, GroupLayout.PREFERRED_SIZE, 465, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();

        jTabbedPane1.addChangeListener(e -> {
            if (jTabbedPane1.getTabCount() <= 1) {
                abiertomusica = false;
            }
            actualizarMenus();
        });
        actualizarMenus();

    }

    private static JFileChooser getJFileChooser() {
        JFileChooser selector = new JFileChooser();
        selector.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Filtro multimedia: audio, video e imagen
        javax.swing.filechooser.FileNameExtensionFilter filtro = new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos multimedia (Audio, Video, Imagen)",
                ".mp3", ".wav", ".flac",".mp4", ".avi", ".mkv",".jpg", ".jpeg", ".png", ".gif"
        );
        selector.setFileFilter(filtro);
        return selector;
    }

    private void actualizarMenus() {
        boolean hayContenido = jTabbedPane1.getTabCount() > 1; // La primera pestaña es la de arrastrar/examinar
        boolean hayMusica = abiertomusica && musica != null;

        // Menú "Editar"
        jMenu2.setEnabled(hayContenido);

        // Opciones dentro de "Archivos"
        jMenu17.setEnabled(hayMusica); // Añadir a favoritos
        jMenu5.setEnabled(hayContenido); // Guardar (y submenús)
    }


    public class PestanaConCerrar extends JPanel {
        public PestanaConCerrar(JTabbedPane tabbedPane, String titulo, Runnable onClose) {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

            JLabel label = new JLabel(titulo + " ");
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            add(label);

            JButton botonCerrar = new JButton("x");
            botonCerrar.setFont(new Font("Arial", Font.BOLD, 12));
            botonCerrar.setMargin(new Insets(0, 4, 0, 4));
            botonCerrar.setFocusable(false);
            botonCerrar.setBorder(BorderFactory.createEtchedBorder());
            botonCerrar.setForeground(Color.RED);
            botonCerrar.setContentAreaFilled(false);

            botonCerrar.addActionListener(e -> {
                int index = tabbedPane.indexOfTabComponent(this);
                if (index != -1 && index != 0) { // evita cerrar la pestaña inicial
                    tabbedPane.remove(index);
                    if (onClose != null) {
                        onClose.run();
                    }
                }
            });

            add(botonCerrar);
        }
    }


}