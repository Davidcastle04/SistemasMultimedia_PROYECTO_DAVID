package es.ujaen.sistemasmultimedia.INTERFAZ;

import es.ujaen.sistemasmultimedia.MULTIMEDIA.IMAGEN;
import es.ujaen.sistemasmultimedia.MULTIMEDIA.MUSICA;
import es.ujaen.sistemasmultimedia.MULTIMEDIA.VIDEO;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UI extends JFrame {

    public Map<JPanel, Object> PANELMAP = new HashMap<>();
    boolean hayCambiosSinGuardar = false;
    public JTextField ArchivoAbierto = new JTextField();
    private final JPanel jPanel1 = new JPanel();
    private final JTabbedPane jTabbedPane1 = new JTabbedPane();
    public boolean abiertomusica=false;
    List<VIDEO> videosAbiertos = new ArrayList<>();
    MUSICA musica = null;
    JSeparator jSeparator1 = new JSeparator();
    Label label3 = new Label();
    JButton examinarArchivoBoton = new JButton();
    Panel panel1 = new Panel();
    Label label1 = new Label();
    JMenuBar jMenuBar1 = new JMenuBar();
    JMenu M_Archivos = new JMenu();
    JMenu Archivos_guardarFichero = new JMenu();
    JMenu Archivo_FAVORITOS = new JMenu();
    JMenuItem guardar_orig_sin_mod = new JMenuItem();
    JMenuItem guardar_mod = new JMenuItem();
    JMenuItem Archivos_abrirFichero = new JMenuItem();
    JMenu Archicos_recientes = new JMenu();
    JMenuItem Archivos_cargarPlaylist = new JMenuItem();
    JMenuItem Archivos_cargarAlbum = new JMenuItem();
    JMenuItem Archivos_addFav = new JMenuItem();
    JMenu Editar = new JMenu();
    JMenuItem Editar_CambiarNombre = new JMenuItem();
    JMenuItem Editar_ModMetadatos = new JMenuItem();
    JMenuItem Editar_AddCaratula = new JMenuItem();
    JMenuItem Editar_AddPlaylist = new JMenuItem();
    JMenuItem Editar_AddAlbum = new JMenuItem();
    JMenu Ayuda = new JMenu();
    JMenuItem abrirAyuda = new JMenuItem(); // Declarar como miembro de la clase
    GroupLayout panel1Layout = new GroupLayout(panel1);
    GroupLayout jPanel1Layout = new GroupLayout(jPanel1);


    public UI() {
        UIManager.put("OptionPane.yesButtonText", "Sí");
        actualizarMenus(); //Añadido
        UIManager.put("OptionPane.noButtonText", "No");
        initComponents();
        abrirFavoritos(); // Cargar los favoritos al iniciar la interfaz
        cargarArchivosRecientes(); // Cargar los recientes al iniciar
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
                                        MUSICA Musica = new MUSICA(videosAbiertos,UI.this);
                                        musica = Musica;
                                        addReciente(file);
                                        JPanel panel = Musica.AUDIO(file,ArchivoAbierto);
                                        PANELMAP.put(panel,Musica);
                                        if (panel != null) {
                                            ((JComponent) panel).putClientProperty("tituloReal", "Musica " + file.getName());
                                        }
                                        jTabbedPane1.addTab(null,panel);
                                        jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                                new PestanaConCerrar(jTabbedPane1, "Musica", () -> {
                                                    abiertomusica = false;
                                                    actualizarMenus();
                                                }));
                                    } else {
                                        if (musica != null) {
                                            musica.addMusica(file);
                                        } else {
                                            musica = new MUSICA(videosAbiertos,UI.this);
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
                                    VIDEO video = new VIDEO(UI.this);
                                    videosAbiertos.add(video);
                                    JPanel panel = video.getVideo(file);
                                    PANELMAP.put(panel,video);
                                    if (panel != null) {
                                        panel.putClientProperty("tituloReal", "Video " + file.getName());
                                    }
                                    addReciente(file);
                                    jTabbedPane1.addTab(null, panel);
                                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                            new PestanaConCerrar(jTabbedPane1, "Video" + file.getName(), UI.this::actualizarMenus));
                                } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png") || filePath.endsWith(".gif")) {

                                    IMAGEN imagen = new IMAGEN(UI.this);
                                    JPanel panel = imagen.img(file);
                                    PANELMAP.put(panel,imagen);
                                    if (panel != null) {
                                        panel.putClientProperty("tituloReal", "Imagen " + file.getName());
                                    }
                                    jTabbedPane1.addTab(null, panel);
                                    addReciente(file);
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

            }
        });

    }
    private void initComponents() {
        // Se elimina la primera sección duplicada de inicialización de componentes
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MultiStudio");
        setIconImage(new ImageIcon("imagenes/logo.jpg").getImage());
        setBackground(new java.awt.Color(153, 153, 153));
        setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));
        jTabbedPane1.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        label3.setText("Arrastre un archivo hacia está ventana o bien pulse en examinar");
        examinarArchivoBoton.setLabel("Examinar Archivos");

        // jPanel1Layout ya está inicializado como miembro de la clase, usarlo directamente
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(examinarArchivoBoton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(examinarArchivoBoton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)
        );


        ImageIcon imagenAdd = new ImageIcon("imagenes/add.png");
        Image imagenEscalada = imagenAdd.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon imagenEscaladaAdd = new ImageIcon(imagenEscalada);
        jTabbedPane1.addTab("",imagenEscaladaAdd, jPanel1);

        jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = jTabbedPane1.getSelectedIndex();
                if (index != -1) {
                    Component comp = jTabbedPane1.getComponentAt(index);
                    if (comp instanceof JComponent) {
                        Object titulo = ((JComponent) comp).getClientProperty("tituloReal");
                        if (titulo != null) {
                            ArchivoAbierto.setHorizontalAlignment(JTextField.CENTER);
                            ArchivoAbierto.setText(acortarTexto("TIPO DE FICHERO ABIERTO: " + titulo, MAX_TEXTO_ARCHIVO));
                        } else {
                            ArchivoAbierto.setHorizontalAlignment(JTextField.CENTER);
                            ArchivoAbierto.setText("ABRA UN FICHERO :) ");
                        }
                    } else {
                        ArchivoAbierto.setText("TIPO DE FICHERO ABIERTO: Desconocido");
                    }
                }
                if (jTabbedPane1.getTabCount() <= 1) {
                    abiertomusica = false;
                }
                actualizarMenus();
            }
        });


        // panel1Layout ya está inicializado como miembro de la clase, usarlo directamente
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 135, Short.MAX_VALUE));
        panel1Layout.setVerticalGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 135, Short.MAX_VALUE));



        ArchivoAbierto.setBackground(new java.awt.Color(153, 153, 255));
        ArchivoAbierto.setForeground(new java.awt.Color(255, 255, 255));
        ArchivoAbierto.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        ArchivoAbierto.setOpaque(true);
        label1.setFont(new java.awt.Font("DejaVu Sans Condensed", 2, 36));



        ArchivoAbierto.setHorizontalAlignment(JTextField.CENTER);
        ArchivoAbierto.setText("ABRA UN FICHERO :) ");
        label1.setText("MultiStudio");
        M_Archivos.setText("Archivos");
        Archivos_guardarFichero.setText("Guardar Fichero");
        guardar_orig_sin_mod.setText("Archivo Original SIN MODIFICAR");
        guardar_mod.setText("Guardar Modificaciones");
        Archivos_abrirFichero.setText("Abrir Fichero");
        Archicos_recientes.setText("Archivos Recientes");
        Archivos_cargarPlaylist.setText("Cargar Playlist");
        Archivo_FAVORITOS.setText("Abrir Favoritos");
        Archivos_cargarAlbum.setText("Cargar Album Fotos");
        Archivos_addFav.setText("Añadir a Favoritos");



        String mensajeAyuda = """
                MultiStudio: Permite abrir, gestionar y reproducir archivos multimedia (audio, video, imágenes).
                Puedes cargar playlists y álbumes de fotos.
                Autor: David Castillo Serrano dcs00037@red.ujaen.es""";


        Editar.setText("Editar");
        Editar_CambiarNombre.setText("Cambiar Nombre Fichero");
        Editar_ModMetadatos.setText("Modificar Metadatos");
        Editar_AddCaratula.setText("Añadir Caratula");
        Editar_AddPlaylist.setText("Crear Nueva PlayList");
        Editar_AddAlbum.setText("Crear Album de Fotos");
        Ayuda.setText("Ayuda");
        abrirAyuda.setText("Abrir Ayuda");


        Archivos_guardarFichero.setIcon(new ImageIcon("imagenes/guardar.png"));
        Archivos_abrirFichero.setIcon(new ImageIcon("imagenes/open.png"));
        Archicos_recientes.setIcon(new ImageIcon("imagenes/historial.png"));
        Archivo_FAVORITOS.setIcon(new ImageIcon("imagenes/favorito2.png"));
        Archivos_cargarPlaylist.setIcon(new ImageIcon("imagenes/playlist.png"));
        Archivos_cargarAlbum.setIcon(new ImageIcon("imagenes/album.png"));
        Archivos_addFav.setIcon(new ImageIcon("imagenes/favorito.png"));
        Editar_CambiarNombre.setIcon(new ImageIcon("imagenes/fichero.png"));
        Editar_ModMetadatos.setIcon(new ImageIcon("imagenes/metadatos.png"));
        Editar_AddCaratula.setIcon(new ImageIcon("imagenes/caratula.png"));
        Editar_AddPlaylist.setIcon(new ImageIcon("imagenes/playlist.png"));
        Editar_AddAlbum.setIcon(new ImageIcon("imagenes/album.png"));
        abrirAyuda.setIcon(new ImageIcon("imagenes/ayuda.png"));


        setJMenuBar(jMenuBar1);


        Editar.add(Editar_CambiarNombre);
        jMenuBar1.add(M_Archivos);
        M_Archivos.add(Archivos_addFav);
        M_Archivos.add(Archivo_FAVORITOS);
        M_Archivos.add(Archivos_cargarAlbum);
        M_Archivos.add(Archivos_cargarPlaylist);
        M_Archivos.add(Archicos_recientes);
        M_Archivos.add(Archivos_abrirFichero);
        Archivos_guardarFichero.add(guardar_orig_sin_mod);
        Archivos_guardarFichero.add(guardar_mod);
        M_Archivos.add(Archivos_guardarFichero);
        Archivos_guardarFichero.setVisible(false);
        Editar.add(Editar_ModMetadatos);
        Editar.add(Editar_AddCaratula);
        Editar.add(Editar_AddPlaylist);
        Editar.add(Editar_AddAlbum);
        jMenuBar1.add(Editar);
        Ayuda.add(abrirAyuda);
        jMenuBar1.add(Ayuda);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator1)
                        .addGroup(layout.createSequentialGroup().addGap(102, 102, 102).addComponent(label1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(ArchivoAbierto, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE).addGap(98, 98, 98))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(47, Short.MAX_VALUE).addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 792, GroupLayout.PREFERRED_SIZE).addGap(43, 43, 43))
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
        actualizarMenus();
        pack();

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
                        MUSICA musica = new MUSICA(videosAbiertos,this);
                        addReciente(archivoSeleccionado);
                        JPanel panel = musica.AUDIO(archivoSeleccionado,ArchivoAbierto);
                        PANELMAP.put(panel,musica);
                        if (panel != null) {
                            ((JComponent) panel).putClientProperty("tituloReal", "Musica " + archivoSeleccionado.getName());
                        }
                        jTabbedPane1.addTab(null,panel);
                        jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                new PestanaConCerrar(jTabbedPane1, "Musica", () -> {
                                    abiertomusica = false;
                                    actualizarMenus();
                                }));
                    } else {
                        if (musica != null) {
                            musica.addMusica(archivoSeleccionado);
                        } else {
                            musica = new MUSICA(videosAbiertos,this);
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
                    VIDEO video = new VIDEO(this);
                    videosAbiertos.add(video);
                    addReciente(archivoSeleccionado);
                    JPanel panel = video.getVideo(archivoSeleccionado);
                    PANELMAP.put(panel,video);
                    if (panel != null) {
                        ((JComponent) panel).putClientProperty("tituloReal", "Video " + archivoSeleccionado.getName());
                    }
                    addReciente(archivoSeleccionado);
                    jTabbedPane1.addTab(null, panel);
                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                            new PestanaConCerrar(jTabbedPane1, "Video" + archivoSeleccionado.getName(), UI.this::actualizarMenus));
                } else if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png") || nombre.endsWith(".gif")) {
                    IMAGEN imagen = new IMAGEN(UI.this);
                    addReciente(archivoSeleccionado);
                    JPanel panel = imagen.img(archivoSeleccionado);
                    PANELMAP.put(panel,imagen);
                    if (panel != null) {
                        ((JComponent) panel).putClientProperty("tituloReal", "Imagen " + archivoSeleccionado.getName());
                    }
                    jTabbedPane1.addTab(null, panel);
                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                            new PestanaConCerrar(jTabbedPane1, "Imagen "+archivoSeleccionado.getName(), UI.this::actualizarMenus));
                }
            }
        });

        Archivos_cargarAlbum.addActionListener(evt -> {
            JFileChooser selector = CargarFicherosINI('a');

            int resultado = selector.showOpenDialog(UI.this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = selector.getSelectedFile();
                System.out.println("Archivo seleccionado: " + archivoSeleccionado.getAbsolutePath());
                abrirColecciones(archivoSeleccionado);
            }

        });

        Archivos_cargarPlaylist.addActionListener(evt -> {
            JFileChooser selector = CargarFicherosINI('p');

            int resultado = selector.showOpenDialog(UI.this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = selector.getSelectedFile();
                System.out.println("Archivo seleccionado: " + archivoSeleccionado.getAbsolutePath());
                abrirColecciones(archivoSeleccionado);
            }
        });

        Archivos_abrirFichero.addActionListener(evt -> {
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
                        MUSICA musica = new MUSICA(videosAbiertos,this);
                        addReciente(archivoSeleccionado);
                        JPanel panel = musica.AUDIO(archivoSeleccionado,ArchivoAbierto);
                        PANELMAP.put(panel,musica);
                        if (panel != null) {
                            ((JComponent) panel).putClientProperty("tituloReal", "Musica " + archivoSeleccionado.getName());
                        }
                        jTabbedPane1.addTab(null,panel);
                        jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                                new PestanaConCerrar(jTabbedPane1, "Musica", () -> {
                                    abiertomusica = false;
                                    actualizarMenus();
                                }));
                    } else {
                        if (musica != null) {
                            musica.addMusica(archivoSeleccionado);
                        } else {
                            musica = new MUSICA(videosAbiertos,this);
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
                    Component contenido = new VIDEO(this).getVideo(archivoSeleccionado);
                    addReciente(archivoSeleccionado);
                    jTabbedPane1.putClientProperty("tituloReal","Video" + archivoSeleccionado.getName());
                    jTabbedPane1.addTab(null, contenido);
                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                            new PestanaConCerrar(jTabbedPane1, "Video" + archivoSeleccionado.getName(), UI.this::actualizarMenus));
                } else if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png") || nombre.endsWith(".gif")) {
                    IMAGEN imagen = new IMAGEN(UI.this);
                    JPanel panel = imagen.img(archivoSeleccionado);
                    PANELMAP.put(panel,imagen);
                    jTabbedPane1.putClientProperty("tituloReal","Imagen" + archivoSeleccionado.getName());
                    addReciente(archivoSeleccionado);
                    jTabbedPane1.addTab(null, panel);
                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                            new PestanaConCerrar(jTabbedPane1, "Imagen "+archivoSeleccionado.getName(), UI.this::actualizarMenus));
                }
            }

        });

        Archivos_addFav.addActionListener(e -> {
            int index = jTabbedPane1.getSelectedIndex();
            if (index != -1) {
                Component componente = jTabbedPane1.getComponentAt(index);
                if (componente instanceof JPanel) {
                    Object objeto = PANELMAP.get(componente);
                    if (objeto instanceof IMAGEN imagen) {
                        File archivoSeleccionado = imagen.getArchivo(); // Método para obtener el archivo
                        addFavoritos(archivoSeleccionado);
                    } else if (objeto instanceof VIDEO video) {
                        File archivoSeleccionado = video.getArchivo(); // Método para obtener el archivo
                        addFavoritos(archivoSeleccionado);
                    } else if (objeto instanceof MUSICA musica) {
                        File archivoSeleccionado = musica.getArchivo(); // Método para obtener el archivo
                        addFavoritos(archivoSeleccionado);
                    } else {
                        JOptionPane.showMessageDialog(this, "El componente seleccionado no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        abrirAyuda.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, mensajeAyuda, "Ayuda", JOptionPane.INFORMATION_MESSAGE);
        });

        Editar_CambiarNombre.addActionListener(e -> {
            int index = jTabbedPane1.getSelectedIndex();
            if (index != -1) {
                Component componente = jTabbedPane1.getComponentAt(index);
                if (componente instanceof JPanel) {
                    Object objeto = PANELMAP.get(componente);
                    if (objeto instanceof IMAGEN imagen) {
                        imagen.TratarArchivo(1, null); // Llamar al método de IMAGEN
                    } else if (objeto instanceof VIDEO video) {
                        video.TratarArchivo(1, null); // Llamar al método de VIDEO
                    } else if (objeto instanceof MUSICA musica) {
                        musica.TratarArchivo(1, null); // Llamar al método de MUSICA
                    }else{
                        JOptionPane.showMessageDialog(this, "El componente seleccionado no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        Editar_AddCaratula.addActionListener(e -> {
            int index = jTabbedPane1.getSelectedIndex();
            if (index != -1) {
                Component componente = jTabbedPane1.getComponentAt(index);
                if (componente instanceof JPanel) {
                    MUSICA musica = (MUSICA) PANELMAP.get(componente);
                    if (musica != null) {
                        musica.TratarArchivo(3, null);
                    } else {
                        JOptionPane.showMessageDialog(this, "El componente seleccionado no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        Editar_AddAlbum.addActionListener(e ->{
            int index = jTabbedPane1.getSelectedIndex();
            if (index != -1) {
                Component componente = jTabbedPane1.getComponentAt(index);
                if (componente instanceof JPanel) {
                    IMAGEN imagen = (IMAGEN) PANELMAP.get(componente);
                    if (imagen != null) {
                        imagen.TratarArchivo(3, null); // Llamar al método de IMAGEN
                    } else {
                        JOptionPane.showMessageDialog(this, "El componente seleccionado no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        Editar_ModMetadatos.addActionListener(evt ->{
            int index = jTabbedPane1.getSelectedIndex();
            if (index != -1) {
                Component componente = jTabbedPane1.getComponentAt(index);
                if (componente instanceof JPanel) {
                    Object objeto = PANELMAP.get(componente);
                    if (objeto instanceof IMAGEN imagen) {
                        imagen.TratarArchivo(2, null); // Llamar al método de IMAGEN
                    } else if (objeto instanceof VIDEO video) {
                        video.TratarArchivo(2, null); // Llamar al método de VIDEO
                    } else if (objeto instanceof MUSICA musica) {
                        musica.TratarArchivo(2, null); // Llamar al método de MUSICA
                    } else {
                        JOptionPane.showMessageDialog(this, "El componente seleccionado no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        jTabbedPane1.addChangeListener(e -> {
            int index = jTabbedPane1.getSelectedIndex();
            if (index != -1) {
                Component comp = jTabbedPane1.getComponentAt(index);
                if (comp instanceof JComponent) {
                    Object titulo = ((JComponent) comp).getClientProperty("tituloReal");
                    if (titulo != null) {
                        ArchivoAbierto.setText(acortarTexto("TIPO DE FICHERO ABIERTO: " + titulo, MAX_TEXTO_ARCHIVO));
                      } else {
                        ArchivoAbierto.setText("ABRA UN FICHERO :) ");
                    }
                } else {
                    ArchivoAbierto.setText("TIPO DE FICHERO ABIERTO: Desconocido");
                }
            }
            if (jTabbedPane1.getTabCount() <= 1) {
                abiertomusica = false;
            }
            actualizarMenus();
        });

        ArchivoAbierto.setColumns(30); 
        ArchivoAbierto.setMaximumSize(new Dimension(300, 30));

    }

    public void actualizarMenus() {
        int selectedIndex = jTabbedPane1.getSelectedIndex();

        // Ocultar o deshabilitar menús por defecto
        M_Archivos.setVisible(true);
        Ayuda.setVisible(true);
        Editar.setVisible(false);
        Archivos_addFav.setVisible(false);
        Archivos_addFav.setEnabled(false);
        Archivos_guardarFichero.setVisible(false);

        Editar_CambiarNombre.setEnabled(false);
        Editar_ModMetadatos.setEnabled(false);
        Editar_AddCaratula.setEnabled(false);
        Editar_AddPlaylist.setEnabled(false);
        Editar_AddAlbum.setEnabled(false);

        // Si no hay pestañas o la pestaña seleccionada es la bienvenida (jPanel1), salir
        if (selectedIndex == -1 || jTabbedPane1.getComponentAt(selectedIndex) == jPanel1) {
            return;
        }

        // Obtener componente y objeto multimedia asociado
        Component selectedComponent = jTabbedPane1.getComponentAt(selectedIndex);
        Object mediaObject = PANELMAP.get(selectedComponent);

        if (mediaObject != null) {
            Editar.setVisible(true);
            Archivos_addFav.setVisible(true);
            Archivos_addFav.setEnabled(true);
            Archivos_guardarFichero.setVisible(true);
            Editar_CambiarNombre.setEnabled(true);

            if (mediaObject instanceof VIDEO) {
                Editar_ModMetadatos.setEnabled(true);
                Editar_AddAlbum.setVisible(false);
                Editar_AddCaratula.setVisible(false);
                Editar_AddPlaylist.setVisible(false);
            } else if (mediaObject instanceof MUSICA) {
                Editar_ModMetadatos.setVisible(true);
                Editar_AddCaratula.setVisible(true);
                Editar_AddPlaylist.setVisible(true);
                Editar_AddAlbum.setVisible(false);
            } else if (mediaObject instanceof IMAGEN) {
                Editar_ModMetadatos.setVisible(true);
                Editar_ModMetadatos.setEnabled(true);
                Editar_AddAlbum.setVisible(true);
                Editar_AddCaratula.setVisible(false);
                Editar_AddPlaylist.setVisible(false);
            }
        }
    }


    private static JFileChooser getJFileChooser() {
        JFileChooser selector = new JFileChooser();
        selector.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Filtro multimedia: audio, video e imagen
        javax.swing.filechooser.FileNameExtensionFilter filtro = new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos multimedia (Audio, Video, Imagen)",
                "mp3", "wav", "flac","mp4", "avi", "mkv","jpg", "jpeg", "png", "gif"
        );
        selector.setFileFilter(filtro);
        return selector;
    }



    public void IMAGENCONF(Boolean r){
        Editar_AddCaratula.setEnabled(r);
        Archivos_addFav.setEnabled(!r);
        Editar_AddPlaylist.setEnabled(r);
    }

    public static class PestanaConCerrar extends JPanel {
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
                if (index != -1 && index != 0) {
                    tabbedPane.remove(index);
                    if (onClose != null) {
                        onClose.run();
                    }
                }
            });

            add(botonCerrar);
        }

        public void modificarTitulo(String nuevoTitulo) {
            JLabel label = (JLabel) getComponent(0);
            label.setText(nuevoTitulo + " ");
            repaint();
        }
    }

    private JFileChooser CargarFicherosINI(char tipo) {
    JFileChooser selector = new JFileChooser();
    selector.setFileSelectionMode(JFileChooser.FILES_ONLY);

    javax.swing.filechooser.FileFilter filtroINI = new javax.swing.filechooser.FileFilter() {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String name = f.getName().toLowerCase();
            if (tipo == 'a') {
                return name.endsWith(".album");
            } else if (tipo == 'p') {
                return name.endsWith(".pylist");
            }
            return false;
        }

        @Override
        public String getDescription() {
            if (tipo == 'a') {
                return "Archivo con extensión .album";
            } else if (tipo == 'p') {
                return "Archivo con extensión .pylist";
            }
            return "Archivos (ALBUM o PYLIST)";
        }
    };
    selector.setFileFilter(filtroINI);
    return selector;
}

    //    public void modificaGuardar(boolean t){
    //        hayCambiosSinGuardar=t;
    //    }

    private void abrirColecciones(File archivo){
        if (archivo.getName().endsWith(".album")){
            try{
            java.util.List<String> lineas = Files.readAllLines(archivo.toPath());

            IMAGEN imagen = new IMAGEN(UI.this);
            JPanel panel = imagen.img(new File(lineas.get(1)));
            PANELMAP.put(panel,imagen);
                if (panel != null) {
                ((JComponent) panel).putClientProperty("tituloReal", "Album  " + archivo.getName());
            }
            jTabbedPane1.addTab(null, panel);
            jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                    new PestanaConCerrar(jTabbedPane1, "Album "+lineas.get(0).split("=")[1], UI.this::actualizarMenus));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"Error en la apertura del album" ,"Error",JOptionPane.ERROR_MESSAGE);
            }
        }

        if(archivo.getName().endsWith(".pylist")){
            try {
                java.util.List<String> lineas = Files.readAllLines(archivo.toPath());
                if(abiertomusica){
                    for(int i =1;i<lineas.size();i++){
                        File archivos = new File(lineas.get(i));
                        if (archivos.exists()) {
                            musica.addMusica(archivos);
                        } else {
                            JOptionPane.showMessageDialog(null,"El fichero "+ archivos.getName() +" no existe o bien ha cambiado de ruta ","Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else{
                    abiertomusica = true;
                    MUSICA musica = new MUSICA(videosAbiertos,this);
                    JPanel panel = musica.AUDIO(new File(lineas.get(1)),ArchivoAbierto);
                    PANELMAP.put(panel,musica);
                    if (panel != null) {
                        ((JComponent) panel).putClientProperty("MUSICA", "Playlist  " + archivo.getName());
                    }
                    jTabbedPane1.addTab(null, panel);
                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                            new PestanaConCerrar(jTabbedPane1, "Playlist "+lineas.get(0).split("=")[1], UI.this::actualizarMenus));

                    for(int i =1;i<lineas.size();i++){
                        File archivos = new File(lineas.get(i));
                        if (archivos.exists()) {
                            musica.addMusica(archivos);
                        } else {
                            JOptionPane.showMessageDialog(null,"El fichero "+ archivos.getName() +" no existe o bien ha cambiado de ruta ","Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"Error en la apertura de la playlist" ,"Error",JOptionPane.ERROR_MESSAGE);
            }
        }


    }

    public void cambiarNombreTAB(String nombre){
        int index = jTabbedPane1.getSelectedIndex();
        if (index != -1) {
            Component componente = jTabbedPane1.getComponentAt(index);
            if (componente instanceof JComponent) {
                ((JComponent) componente).putClientProperty("tituloReal", nombre);
                jTabbedPane1.setTitleAt(index, nombre);
                PestanaConCerrar pestana = (PestanaConCerrar) jTabbedPane1.getTabComponentAt(index);
                if (pestana != null) {
                    pestana.modificarTitulo(nombre);
                }
            }
        }
    }

    public void abrirFichero(File file){
        if(file.exists()){
            String filePath = file.getAbsolutePath().toLowerCase();
            if (filePath.endsWith(".mp3") || filePath.endsWith(".wav") || filePath.endsWith(".flac")) {
                if (!abiertomusica) {
                    abiertomusica = true;
                    MUSICA musica = new MUSICA(videosAbiertos,this);
                    addReciente(file);
                    JPanel panel = musica.AUDIO(file,ArchivoAbierto);
                    PANELMAP.put(panel,musica);
                    if (panel != null) {
                        ((JComponent) panel).putClientProperty("tituloReal", "Musica " + file.getName());
                    }
                    jTabbedPane1.addTab(null,panel);
                    jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                            new PestanaConCerrar(jTabbedPane1, "Musica", () -> {
                                abiertomusica = false;
                                actualizarMenus();
                            }));
                } else {
                    if (musica != null) {
                        musica.addMusica(file);
                    } else {
                        musica = new MUSICA(videosAbiertos,this);
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
                VIDEO video = new VIDEO(this);
                videosAbiertos.add(video);
                addReciente(file);
                JPanel panel = video.getVideo(file);
                PANELMAP.put(panel,video);
                if (panel != null) {
                    ((JComponent) panel).putClientProperty("tituloReal", "Video " + file.getName());
                }
                addReciente(file);
                jTabbedPane1.addTab(null, panel);
                jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                        new PestanaConCerrar(jTabbedPane1, "Video" + file.getName(), UI.this::actualizarMenus));
            } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png") || filePath.endsWith(".gif")) {
                IMAGEN imagen = new IMAGEN(UI.this);
                addReciente(file);
                JPanel panel = imagen.img(file);
                PANELMAP.put(panel,imagen);
                if (panel != null) {
                    ((JComponent) panel).putClientProperty("tituloReal", "Imagen " + file.getName());
                }
                jTabbedPane1.addTab(null, panel);
                jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                        new PestanaConCerrar(jTabbedPane1, "Imagen "+file.getName(), UI.this::actualizarMenus));

            }
        }else{
            JOptionPane.showMessageDialog(null,"El fichero "+file.getName()+" no existe o bien ha cambiado de ruta ","Error",JOptionPane.ERROR_MESSAGE);
        }



    }

    private void cargarArchivosRecientes() {
        try {
            File archivoRecientes = new File("recientes.txt");
            Archicos_recientes.removeAll(); // Limpia los elementos previos del menú

            if (archivoRecientes.exists()) {
                List<String> lineasExistentes = Files.readAllLines(archivoRecientes.toPath());
                // Asegurar que solo se procesan hasta 5, en caso de que el archivo tenga más
                List<String> lineasAMostrar = lineasExistentes.size() > 5 ? lineasExistentes.subList(0, 5) : lineasExistentes;

                for (String linea : lineasAMostrar) {
                    if (linea != null && !linea.trim().isEmpty()) {
                        File file = new File(linea.trim());
                        JMenuItem menuItem = new JMenuItem(file.getName()); // Mostrar solo el nombre del archivo
                        menuItem.setToolTipText(file.getAbsolutePath());   // Ruta completa como tooltip
                        menuItem.addActionListener(ef -> abrirFichero(file));
                        Archicos_recientes.add(menuItem);
                    }
                }
            }

            Archicos_recientes.revalidate();
            Archicos_recientes.repaint();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar archivos recientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addReciente(File archivoSeleccionado) {
        try {
            File archivoRecientes = new File("recientes.txt");
            List<String> lineasExistentes = archivoRecientes.exists()
                    ? Files.readAllLines(archivoRecientes.toPath())
                    : new ArrayList<>();

            String nuevaRuta = archivoSeleccionado.getAbsolutePath();
            lineasExistentes.removeIf(ruta -> ruta.equals(nuevaRuta)); // Eliminar todas las ocurrencias para moverla al principio
            lineasExistentes.add(0, nuevaRuta);

            if (lineasExistentes.size() > 5) {
                lineasExistentes = lineasExistentes.subList(0, 5);
            }

            Files.write(archivoRecientes.toPath(), lineasExistentes); // Escribir la lista actualizada
            cargarArchivosRecientes(); // Actualizar el menú
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar archivo reciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addFavoritos(File f) {
        try {
            // Leer las líneas existentes en el archivo favoritos.txt
            File archivoFavoritos = new File("favoritos.txt");
            java.util.List<String> lineasExistentes = archivoFavoritos.exists()
                    ? Files.readAllLines(archivoFavoritos.toPath())
                    : new java.util.ArrayList<>();

            // Verificar si la ruta ya existe y eliminarla si está presente
            String nuevaRuta = f.getAbsolutePath();
            lineasExistentes.remove(nuevaRuta);

            // Añadir la nueva ruta al inicio de la lista
            lineasExistentes.add(0, nuevaRuta);

            // Limitar la lista a los últimos 10 elementos
            if (lineasExistentes.size() > 10) {
                lineasExistentes = lineasExistentes.subList(0, 10);
            }

            // Escribir la lista actualizada en el archivo
            try (FileWriter fw = new FileWriter(archivoFavoritos, false)) { // Sobrescribir el archivo
                for (String linea : lineasExistentes) {
                    fw.write(linea + "\n");
                }
            }

            // Mostrar mensaje al usuario
            JOptionPane.showMessageDialog(null, "Archivo añadido a favoritos. Quedan " + (10 - lineasExistentes.size()) + " espacios disponibles.", "Favoritos", JOptionPane.INFORMATION_MESSAGE);

            // Actualizar el menú de favoritos
            abrirFavoritos();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al manejar archivos favoritos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void abrirFavoritos() {
        try {
            // Leer las líneas existentes en el archivo favoritos.txt
            File archivoFavoritos = new File("favoritos.txt");
            List<String> lineasExistentes = archivoFavoritos.exists()
                    ? Files.readAllLines(archivoFavoritos.toPath())
                    : new ArrayList<>();

            Archivo_FAVORITOS.removeAll(); // Limpia los elementos previos del menú

            for (String linea : lineasExistentes) {
                File archivo = new File(linea);
                String nombreArchivo = archivo.getName();

                JMenuItem menuItem = new JMenuItem(nombreArchivo, createIconX());
                menuItem.setToolTipText(nombreArchivo);

                // Acción para abrir el archivo
                menuItem.addActionListener(e -> abrirFichero(archivo));

                // Menú contextual (clic derecho) para eliminar
                menuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            int confirm = JOptionPane.showConfirmDialog(null,
                                    "¿Eliminar de favoritos?", "Confirmar", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                lineasExistentes.remove(linea);
                                try (FileWriter fw = new FileWriter(archivoFavoritos, false)) {
                                    for (String ruta : lineasExistentes) {
                                        fw.write(ruta + "\n");
                                    }
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Error al actualizar favoritos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                JOptionPane.showMessageDialog(null, "Se ha borrado de favoritos. Le quedan " + (10 - lineasExistentes.size()) + " espacios disponibles.", "Favoritos", JOptionPane.INFORMATION_MESSAGE);
                                abrirFavoritos();
                            }
                        }
                    }
                });

                Archivo_FAVORITOS.add(menuItem);
            }

            Archivo_FAVORITOS.revalidate();
            Archivo_FAVORITOS.repaint();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al manejar archivos favoritos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon createIconX() {
        int width = 16, height = 16;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));
        g.drawLine(3, 3, 13, 13);
        g.drawLine(3, 13, 13, 3);
        g.dispose();
        return new ImageIcon(image);
    }

    private static final int MAX_TEXTO_ARCHIVO = 40;

    private String acortarTexto(String texto, int max) {
        if (texto.length() > max) {
            return texto.substring(0, max - 3) + "...";
        }
        return texto;
    }

}
