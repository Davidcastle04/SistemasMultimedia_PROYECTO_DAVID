package es.ujaen.sistemasmultimedia.MULTIMEDIA;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.*;
import com.drew.metadata.exif.*;
import es.ujaen.sistemasmultimedia.UI;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class IMAGEN  {

    UI interfaz;
    JButton Boton_borrarImagen = new JButton("     Borrar Fotografía     ");
    JButton Boton_addAlbum = new JButton("    Añadir a un álbum    ");
    JButton Boton_verAlbum = new JButton("Ver más fotos del mismo álbum");
    Label Metadatos_title = new Label("Metadatos de la imagen");
    Label Met_fyh = new Label("Fecha y Hora");
    Label Met_ModCamara = new Label("Modelo de cámara ");
    Label Met_Tam = new Label("Tamaño ");
    Label Met_ISO = new Label("ISO");
    Label Met_VelObt = new Label("Velocidad de obturación ");
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();
    JButton zoomInBtn = new JButton("Zoom +");
    JButton zoomOutBtn = new JButton("Zoom -");
    JButton rotateRightBtn = new JButton("Rotar ➡");
    JButton rotateLeftBtn = new JButton("Rotar ⬅");
    JButton resetZoomBtn = new JButton("Reset");
    File foto;
    ZoomableImagePanel zoomPanel;

    GroupLayout jPanel4Layout = new GroupLayout(jPanel4);

    public IMAGEN(UI i ) {
        System.out.println("Inicializado el panel de imágenes");
        interfaz = i;
        interfaz.IMAGENCONF(false);
    }

    public JPanel img(File archivo) {
        Boton_borrarImagen.setForeground(new Color(255, 0, 51));

        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(34)
                                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, 484, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(Boton_borrarImagen)
                                        .addComponent(Boton_addAlbum)
                                        .addComponent(Boton_verAlbum)
                                        .addComponent(Metadatos_title, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Met_fyh, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Met_ModCamara, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Met_Tam, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Met_ISO, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Met_VelObt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(30)
                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(Boton_borrarImagen)
                                                .addGap(18)
                                                .addComponent(Boton_addAlbum)
                                                .addGap(18)
                                                .addComponent(Boton_verAlbum)
                                                .addGap(20)
                                                .addComponent(Metadatos_title, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Met_fyh, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Met_ModCamara, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Met_Tam, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Met_ISO, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Met_VelObt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel5.setLayout(new BorderLayout());
        cargarImagen(archivo);
        extraerMetadatos(archivo);

        JPanel controlPanel = new JPanel(); // Panel para botones de control
        controlPanel.add(zoomInBtn);
        controlPanel.add(zoomOutBtn);
        controlPanel.add(rotateLeftBtn);
        controlPanel.add(rotateRightBtn);
        controlPanel.add(resetZoomBtn);

        jPanel5.add(controlPanel, BorderLayout.SOUTH);

        Boton_borrarImagen.addActionListener(evt -> {
            int res = JOptionPane.showConfirmDialog(null,
                    "¿Estás seguro de borrar " + archivo.getName() + " DEFINITIVAMENTE????",
                    "Borrar Imagen",
                    JOptionPane.YES_NO_OPTION
            );

            if (res == JOptionPane.YES_OPTION) {
                if (archivo.delete()) {
                    // Buscar el JTabbedPane que contiene jPanel4
                    Container parent = jPanel4.getParent();
                    while (parent != null && !(parent instanceof JTabbedPane)) {
                        parent = parent.getParent();
                    }

                    if (parent instanceof JTabbedPane tabs) {
                        int index = tabs.indexOfComponent(jPanel4);
                        if (index != -1) {
                            tabs.remove(index);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo borrar el archivo.");
                }
            }
        });


        Boton_addAlbum.addActionListener(evt -> {
            crearAlbum();
        });


        Boton_verAlbum.addActionListener(evt -> {
            File carpetaAlbums = new File("../ALBUMS");
            File[] archivos = carpetaAlbums.listFiles((dir, name) -> name.toLowerCase().endsWith(".album"));
            boolean encontrada = false;

            if (archivos != null) {
                for (File files : archivos) {
                    try {
                        java.util.List<String> lineas = Files.readAllLines(files.toPath());
                        for (String linea : lineas) {
                            if (linea.trim().equals(archivo.getAbsolutePath())) {
                                encontrada = true;

                                // Mostrar previsualizaciones del álbum
                                java.util.List<ImageIcon> miniaturas = new ArrayList<>();
                                List<File> fotos = new ArrayList<>();
                                for (String ruta : lineas) {
                                    File foto = new File(ruta.trim());
                                    if (foto.exists()) {
                                        fotos.add(foto);
                                        ImageIcon icon = new ImageIcon(foto.getAbsolutePath());
                                        // Escalar la imagen para la miniatura
                                        Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                                        miniaturas.add(new ImageIcon(img));
                                    }
                                }

                                // Crear panel con miniaturas
                                JPanel panel = new JPanel();
                                panel.setLayout(new FlowLayout());
                                for (int i = 0; i < miniaturas.size(); i++) {
                                    JLabel label = new JLabel(miniaturas.get(i));
                                    File foto = fotos.get(i);
                                    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                    label.addMouseListener(new MouseAdapter() {
                                        public void mouseClicked(MouseEvent e) {
                                            abrirfoto(foto);
                                        }
                                    });
                                    panel.add(label);
                                }

                                String titulo = "Álbum sin nombre";

                                if (lineas.get(0).contains("NOMBRE_ALBUM=")) {
                                    String[] partes = lineas.get(0).split("NOMBRE_ALBUM=");
                                    if (partes.length > 1 && !partes[1].trim().isEmpty()) {
                                        titulo = "Fotos del álbum: " + partes[1].trim();
                                    } else {
                                        titulo = "Fotos del álbum: Álbum sin nombre";
                                    }
                                } else {
                                    titulo = "Fotos del álbum: Álbum sin nombre";
                                }

                                JOptionPane.showMessageDialog(null, panel, titulo, JOptionPane.PLAIN_MESSAGE);

                                return; // Salir después de mostrar el primer álbum encontrado
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!encontrada) {
                JOptionPane.showMessageDialog(null, "Esta imagen no está en ningún álbum", "No encontrada", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        return jPanel4;
    }

    private void crearAlbum(){
        String[] opciones = {"Álbum existente", "Álbum nuevo"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "¿Deseas guardar la imagen en un álbum existente o crear uno nuevo?",
                "Añadir a álbum",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == 0) { // Álbum existente
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Selecciona el archivo .album del álbum");
            chooser.setFileFilter(new FileNameExtensionFilter("Ficheros .album", "album"));

            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File iniFile = chooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(iniFile, true)) {
                    writer.write(foto.getAbsolutePath() + System.lineSeparator());
                    JOptionPane.showMessageDialog(null, "Imagen añadida al álbum existente.");

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al escribir en el álbum: " + e.getMessage());
                }
            }

        } else if (seleccion == 1) { // Álbum nuevo
            String nombre = JOptionPane.showInputDialog(null, "Introduce el nombre del nuevo álbum:");
            if (nombre != null && !nombre.trim().isEmpty()) {

                File iniFil = new File("../ALBUMS/"+nombre+".album");
                try(FileWriter writer = new FileWriter(iniFil, true)) {
                    writer.write("NOMBRE_ALBUM="+nombre+System.lineSeparator());
                    writer.write(foto.getAbsolutePath() + System.lineSeparator());
                    JOptionPane.showMessageDialog(null, "Imagen añadida al álbum existente.");

                }catch(IOException se){
                    JOptionPane.showMessageDialog(null, "Error al escribir en el álbum: " + se.getMessage());
                }
                JOptionPane.showMessageDialog(null, "Álbum '" + nombre + "' creado y se añadió la imagen.");
            }
        }
    }

    public void cargarImagen(File f) {
        foto=f;
        try {
            BufferedImage imagen = ImageIO.read(f);
            if (imagen != null) {
                zoomPanel = new ZoomableImagePanel(imagen);
                JScrollPane scrollPane = new JScrollPane(zoomPanel);
                jPanel5.removeAll();
                jPanel5.add(scrollPane, BorderLayout.CENTER);
                jPanel5.revalidate();
                jPanel5.repaint();

                // Listeners:
                zoomInBtn.addActionListener(e -> {
                    zoomPanel.zoom += 0.1;
                    zoomPanel.repaint();
                });

                zoomOutBtn.addActionListener(e -> {
                    zoomPanel.zoom = Math.max(0.1, zoomPanel.zoom - 0.1);
                    zoomPanel.repaint();
                });

                rotateRightBtn.addActionListener(e -> zoomPanel.rotarDerecha());
                rotateLeftBtn.addActionListener(e -> zoomPanel.rotarIzquierda());
                resetZoomBtn.addActionListener(e -> zoomPanel.resetZoom());

            } else {
                System.err.println("No se pudo cargar la imagen.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirfoto(File f ){

        IMAGEN imagen = new IMAGEN(interfaz);
        JPanel panel = imagen.img(f);
        interfaz.PANELMAP.put(panel,imagen);
        Component contenido = panel;

        JTabbedPane jTabbedPane1 = (JTabbedPane)jPanel4.getParent();
        jTabbedPane1.addTab(null,contenido);
        jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1,
                new UI.PestanaConCerrar(jTabbedPane1, "Imagen "+f.getName(), () -> {
                    // Acción al cerrar
                    UI a = (UI)jTabbedPane1.getParent();
                    a.actualizarMenus();
                    // Opcional: Cerrar/eliminar contenido
                    jTabbedPane1.remove(contenido);
                }
                ));
    }

    public void extraerMetadatos(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            String fechaHora = "Desconocida";
            String modeloCamara = "Desconocido";
            String tamnio = (file.length() / 1024) + " KB";
            String iso = "Desconocido";
            String velocidadObturacion = "Desconocida";

            ExifSubIFDDirectory exifSub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (exifSub != null) {
                Date date = exifSub.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (date != null) fechaHora = date.toString();

                Integer isoVal = exifSub.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
                if (isoVal != null) iso = isoVal.toString();

                String vel = exifSub.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
                if (vel != null) velocidadObturacion = vel + " seg";
            }

            ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (ifd0 != null) {
                String modelo = ifd0.getString(ExifIFD0Directory.TAG_MODEL);
                if (modelo != null) modeloCamara = modelo;
            }

            ModificaMetadatos(fechaHora, modeloCamara, tamnio, iso, velocidadObturacion);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ModificaMetadatos(String fyh, String ModCamara, String Tam, String ISO, String VelObt) {
        Met_fyh.setText("Fecha y Hora: " + fyh);
        Met_ModCamara.setText("Modelo de la cámara: " + ModCamara);
        Met_Tam.setText("Tamaño: " + Tam);
        Met_ISO.setText("ISO: " + ISO);
        Met_VelObt.setText("Velocidad de Obturación: " + VelObt);
        jPanel4.repaint();
    }

    static class ZoomableImagePanel extends JPanel {
        private final BufferedImage originalImage;
        private double zoom = 1.0;
        private double rotation = 0.0;
        private Point dragStartPoint = null;
        private double offsetX = 0, offsetY = 0;


        public ZoomableImagePanel(BufferedImage image) {
            this.originalImage = image;
            this.setBackground(Color.BLACK);
            ajustarZoomInicial();

            addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    ajustarZoomInicial();
                    repaint();
                }
            });

            addMouseWheelListener(e -> {
                if (e.getPreciseWheelRotation() < 0) {
                    zoom += 0.1;
                } else {
                    zoom = Math.max(0.1, zoom - 0.1);
                }
                repaint();
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dragStartPoint = e.getPoint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragStartPoint != null) {
                        double dx = e.getX() - dragStartPoint.getX();
                        double dy = e.getY() - dragStartPoint.getY();
                        offsetX += dx;
                        offsetY += dy;
                        dragStartPoint = e.getPoint();
                        repaint();
                    }
                }
            });

        }

        public void rotarDerecha() {
            rotation += Math.PI / 2;
            repaint();
        }

        public void rotarIzquierda() {
            rotation -= Math.PI / 2;
            repaint();
        }

        public void resetZoom() {
            ajustarZoomInicial();
            offsetX = 0;
            offsetY = 0;
            repaint();
        }


        private void ajustarZoomInicial() {
            if (originalImage != null && getParent() != null) {
                int pw = getParent().getWidth();
                int ph = getParent().getHeight();
                if (pw > 0 && ph > 0) {
                    double zw = (double) pw / originalImage.getWidth();
                    double zh = (double) ph / originalImage.getHeight();
                    zoom = Math.min(zw, zh);
                }
            } else {
                zoom = 1.0;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (originalImage != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int w = originalImage.getWidth();
                int h = originalImage.getHeight();
                int newW = (int) (w * zoom);
                int newH = (int) (h * zoom);

                g2d.translate((double) getWidth() / 2 + offsetX, (double) getHeight() / 2 + offsetY);
                g2d.rotate(rotation);
                g2d.translate(-newW / 2, -newH / 2);

                g2d.drawImage(originalImage, 0, 0, newW, newH, this);
            }
        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 600);
        }
    }

    public void TratarArchivo(int i , Object t ) {
        switch (i) {
            case 1:
                // Cambiar de nombre fichero: pedir nombre al usuario
                String nuevoNombre = JOptionPane.showInputDialog(null, "Introduce el nuevo nombre del archivo:");
                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    File file = new File(foto.getParent(), nuevoNombre);
                    if (!foto.renameTo(file)) {
                        JOptionPane.showMessageDialog(null, "Error al renombrar el archivo");
                    } else {
                        JOptionPane.showMessageDialog(null, "Archivo renombrado correctamente.");
                        interfaz.cambiarNombreTAB(nuevoNombre);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nombre no válido o cancelado.");
                }
                break;

            case 2:
                try {
                    // Crear un panel con campos para rellenar
                    JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

                    JTextField isoField = new JTextField();
                    JTextField fechaYhoraField = new JTextField();
                    JTextField velObtField = new JTextField();
                    JTextField modeloField = new JTextField();

                    panel.add(new JLabel("ISO de la cámara:"));
                    panel.add(isoField);
                    panel.add(new JLabel("Fecha y hora (YYYY:MM:DD HH:MM:SS):"));
                    panel.add(fechaYhoraField);
                    panel.add(new JLabel("Velocidad de obturación (ej: 1/200):"));
                    panel.add(velObtField);
                    panel.add(new JLabel("Modelo de la cámara:"));
                    panel.add(modeloField);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Modificar Metadatos",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result != JOptionPane.OK_OPTION) {
                        JOptionPane.showMessageDialog(null, "Operación cancelada.");
                        break;
                    }

                    String iso = isoField.getText().trim();
                    String fechaYhora = fechaYhoraField.getText().trim();
                    String velObt = velObtField.getText().trim();
                    String modelo = modeloField.getText().trim();

                    if (iso.isEmpty() || fechaYhora.isEmpty() || velObt.isEmpty() || modelo.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Todos los campos deben estar completos.");
                        break;
                    }

                    // Leer los metadatos existentes
                    JpegImageMetadata jpegMetadata = (JpegImageMetadata) Imaging.getMetadata(foto);
                    TiffOutputSet outputSet = (jpegMetadata != null && jpegMetadata.getExif() != null)
                            ? jpegMetadata.getExif().getOutputSet()
                            : new TiffOutputSet();

                    TiffOutputDirectory exifDir = outputSet.getOrCreateExifDirectory();

                    exifDir.removeField(ExifTagConstants.EXIF_TAG_ISO);
                    exifDir.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                    exifDir.removeField(ExifTagConstants.EXIF_TAG_EXPOSURE_TIME);
                    exifDir.removeField(ExifTagConstants.EXIF_TAG_MODEL_2);

                    exifDir.add(ExifTagConstants.EXIF_TAG_ISO, (short) Integer.parseInt(iso));
                    exifDir.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, fechaYhora);

                    // Convertir a RationalNumber (por ejemplo: 1/200 = 0.005)
                    double fraccion = 1.0;
                    if (velObt.contains("/")) {
                        String[] partes = velObt.split("/");
                        fraccion = Double.parseDouble(partes[0]) / Double.parseDouble(partes[1]);
                    } else {
                        fraccion = Double.parseDouble(velObt);
                    }
                    RationalNumber exposureTime = RationalNumber.valueOf(fraccion);
                    exifDir.add(ExifTagConstants.EXIF_TAG_EXPOSURE_TIME, exposureTime);
                    try{
                        exifDir.add(ExifTagConstants.EXIF_TAG_USER_COMMENT, modelo);
                    } catch (ImageWriteException e) {
                        System.out.printf("Error al escribir el modelo de la cámara: %s%n", e.getMessage());
                    }

                    ModificaMetadatos(fechaYhora, modelo, (foto.length() / (1024 * 1024)) + " MB", iso, velObt);

                    File nuevoArchivo = new File(foto.getParent(), foto.getName());
                    try (OutputStream os = new FileOutputStream(nuevoArchivo)) {
                        new ExifRewriter().updateExifMetadataLossless(foto, os, outputSet);
                    }

                    JOptionPane.showMessageDialog(null, "Metadatos modificados correctamente.");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al modificar los metadatos: " + e.getMessage());
                }
                break;


            case 3:
                // Crear Album de Fotos
                crearAlbum();
                break;
        }
    }
}