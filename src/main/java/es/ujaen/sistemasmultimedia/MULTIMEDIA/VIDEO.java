package es.ujaen.sistemasmultimedia.MULTIMEDIA;

import es.ujaen.sistemasmultimedia.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class VIDEO {
    private static final Logger log = LoggerFactory.getLogger(VIDEO.class);
    EmbeddedMediaPlayerComponent mediaPlayerComponent;
    UI interfaz;
    File archivoVideo;
    JPanel jPanel3 = new JPanel();
    GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
    JPanel panel2 = new JPanel();
    JSlider BARRATIEMPO = new JSlider();
    Label timer = new Label();
    JButton retroceso = new JButton();
    JButton pausaANDplay = new JButton();
    JButton avanzar = new JButton();
    JSlider BARRAVOLUMEN = new JSlider();
    JLabel jLabel1 = new JLabel();
    JButton borrarVideo = new JButton();
    JButton addVideosFav = new JButton();
    Label Metadatos = new Label();
    Label NombreArchivo = new Label();
    Label Tamanio = new Label();
    Label Duracion = new Label();
    Label Formato = new Label();
    Label Fecha = new Label();
    Label bitrate = new Label();
    Label Titulo = new Label();

    public VIDEO(UI i ) {
        interfaz = i;
        i.InicioVIDEO();
        jPanel3.setLayout(jPanel3Layout);
    }

    public JPanel getVideo(File video) {
        archivoVideo = video;
        obtenerMetadatos(video,null,null,null);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        panel2.removeAll();
        panel2.setLayout(new BorderLayout());
        panel2.add(mediaPlayerComponent, BorderLayout.CENTER);
        // Poner panel2 de color azul al principio
        panel2.setBackground(Color.BLUE);

        panel2.revalidate();
        panel2.repaint();

        SwingUtilities.invokeLater(() -> {
            if (mediaPlayerComponent.isDisplayable()) {
                mediaPlayerComponent.mediaPlayer().media().play(archivoVideo.getAbsolutePath());
            } else {
                // Esperamos hasta que sea visible
                panel2.addHierarchyListener(e -> {
                    if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && panel2.isDisplayable()) {
                        mediaPlayerComponent.mediaPlayer().media().play(archivoVideo.getAbsolutePath());
                    }
                });
            }
        });


        timer.setText("--:-- / --:--");

        retroceso.setLabel("◀ Retroceder");

        pausaANDplay.setLabel(" ▶ Pausa/Play ");

        avanzar.setLabel("Avanzar ▶");

        jLabel1.setText("Volumen");
        borrarVideo.setForeground(new Color(255, 0, 102));
        borrarVideo.setText("Eliminar Video");
        addVideosFav.setText("Añadir a Videos Favoritos");
        Metadatos.setText("Metadatos");
//        NombreArchivo.setText("Nombre del archivo ");
//        Tamanio.setText("Tamaño ");
//        Duracion.setText("Duración");
//        Formato.setText("Formato ");
//        Fecha.setText("Fecha de creación/modificación");
//        bitrate.setText("Bitrate total");
//        Titulo.setText("Titulo");
        BARRATIEMPO.setValue(0);
        BARRAVOLUMEN.setValue(100);


        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(timer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(retroceso, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(26, 26, 26)
                                                .addComponent(pausaANDplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(26, 26, 26)
                                                .addComponent(avanzar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(29, 29, 29)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(BARRAVOLUMEN, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(panel2, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                                                        .addComponent(BARRATIEMPO, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))
                                                .addGap(27, 27, 27)
                                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(borrarVideo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(addVideosFav, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                                                .addComponent(Metadatos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(Tamanio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(NombreArchivo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(Duracion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(Formato, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(Fecha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(bitrate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(Titulo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(panel2, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(borrarVideo)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(addVideosFav)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Metadatos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(1, 1, 1)
                                                .addComponent(NombreArchivo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(2, 2, 2)
                                                .addComponent(Tamanio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(1, 1, 1)
                                                .addComponent(Duracion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(2, 2, 2)
                                                .addComponent(Formato, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(2, 2, 2)
                                                .addComponent(Fecha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(1, 1, 1)
                                                .addComponent(bitrate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(1, 1, 1)
                                                .addComponent(Titulo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BARRATIEMPO, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(1, 1, 1)
                                                .addComponent(timer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel1)
                                                        .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(pausaANDplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(retroceso, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(avanzar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(BARRAVOLUMEN, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(22, Short.MAX_VALUE))
        );

        borrarVideo.addActionListener( e -> BorrarVideo(archivoVideo));

        addVideosFav.addActionListener( e ->{
            JOptionPane.showMessageDialog(null, "Video"+archivoVideo.getName()+" Añadido a favoritos");
            interfaz.addFavoritos(archivoVideo);
        });
        retroceso.addActionListener( e -> desplazamiento(false));
        avanzar.addActionListener( e -> desplazamiento(true));
        pausaANDplay.addActionListener( e -> pausaANDPLAY());
        BARRATIEMPO.addChangeListener(e -> {
            if (BARRATIEMPO.getValueIsAdjusting()) {
                float posicion = BARRATIEMPO.getValue() / 100.0f;
                mediaPlayerComponent.mediaPlayer().controls().setPosition(posicion);
            }
        });

        mediaPlayerComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    boolean fullScreen = mediaPlayerComponent.mediaPlayer().fullScreen().isFullScreen();
                    mediaPlayerComponent.mediaPlayer().fullScreen().set(!fullScreen);
                    System.out.printf("Full screen: %s%n", !fullScreen);
                }
            }
        });

       mediaPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
           @Override
           public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
               long duration = mediaPlayer.media().info().duration();
               if (duration > 0) {
                   int progress = (int) ((newTime * 100) / duration);
                   SwingUtilities.invokeLater(() -> {
                       BARRATIEMPO.setValue(progress);
                       String tiempoActual = formatTime(newTime);
                       String tiempoTotal = formatTime(duration);
                       Duracion.setText("Duración: " + tiempoTotal);
                       cambiarTIMER(tiempoActual, tiempoTotal);
                   });
               }
           }
       });


    BARRAVOLUMEN.setValue(100); // Establece el valor inicial de la barra al 100%
    mediaPlayerComponent.mediaPlayer().audio().setVolume(100); // Establece el volumen inicial al 100%
    BARRAVOLUMEN.addChangeListener(e -> mediaPlayerComponent.mediaPlayer().audio().setVolume(BARRAVOLUMEN.getValue()));
        return jPanel3;
    }

    private void BorrarVideo(File f) {
        int n = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar el video?", "Eliminar video", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            if (f.delete()) {
                System.out.println("El archivo fue eliminado con éxito");
            } else {
                System.out.println("El archivo no pudo ser eliminado");
            }
        }
    }

    private void modificarMetadatos(String name , String tam , String Dur , String For , String Fecha , String Bit , String Tit){
        NombreArchivo.setText("Nombre del Archivo "+ name);
        Tamanio.setText("Tamaño "+tam);
        Duracion.setText("Duración "+Dur);
        Formato.setText("Formato "+For);
        this.Fecha.setText("Fecha de Creacion: "+Fecha);
        bitrate.setText("Bitrate "+Bit);
        Titulo.setText("Titulo "+Tit);
        this.jPanel3.revalidate();
        this.jPanel3.repaint();
    }
    private void pausaANDPLAY() {
        if (mediaPlayerComponent.mediaPlayer().status().isPlaying()) {
            mediaPlayerComponent.mediaPlayer().controls().pause();
            System.out.printf("pause");
        } else {
            mediaPlayerComponent.mediaPlayer().controls().play();
            System.out.printf("play");
        }
    }

    private void desplazamiento(boolean adelante) {
        long salto = 10000; // 10 segundos en milisegundos
        if (adelante) {
            mediaPlayerComponent.mediaPlayer().controls().skipTime(salto);
        } else {
            mediaPlayerComponent.mediaPlayer().controls().skipTime(-salto);
        }
    }
    public void cambioPestana(boolean tr){
        /** NO UTILIZAR YA QUE DA FALLOS
         * @Deprecated
         */
//        if(tr){
//            mediaPlayerComponent.release();
//        } else {
//            panel2.removeAll();
//            panel2.setLayout(new BorderLayout());
//            mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
//            panel2.add(mediaPlayerComponent, BorderLayout.CENTER);
//            panel2.revalidate();
//            panel2.repaint();
//            SwingUtilities.invokeLater(() -> mediaPlayerComponent.mediaPlayer().media().play(archivoVideo.getAbsolutePath()));
//
//            mediaPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
//                @Override
//                public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
//                    long duration = mediaPlayer.media().info().duration();
//                    String tiempoActual = formatTime(newTime);
//                    String tiempoFinal = formatTime(duration);
//                    cambiarTIMER(tiempoActual, tiempoFinal);
//                }
//            });
//        }
    }

    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void cambiarTIMER(String tiempoActual , String tiempoFinal) {
        String tiempo = tiempoActual + " / " + tiempoFinal;
        timer.setText(tiempo);
    }

    private void obtenerMetadatos(File videoFile , String bitrate , String Fecha , String titulo) {
        try {
            Parser parser = new AutoDetectParser();
            ContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();

            try (FileInputStream inputStream = new FileInputStream(videoFile)) {
                parser.parse(inputStream, handler, metadata, context);
            }

            String nombre = videoFile.getName();
            long tamanoBytes = videoFile.length();
            String tamanoKB = String.format("%.2f KB", (double) tamanoBytes / 1024);
            String formato = metadata.get(Metadata.CONTENT_TYPE);

            BasicFileAttributes attributes = Files.readAttributes(videoFile.toPath(), BasicFileAttributes.class);
            Date fechaCreacion = new Date(attributes.creationTime().toMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fechaFormateada = sdf.format(fechaCreacion);

            //String duracionStr =metadata.get("");
            String bitrateStr = metadata.get("tika:metadata:com.github.caprica:vlcj:bitrate");
            String tituloStr = metadata.get("title");

            //String duracionFormateada = getString(duracionStr);

            String bitrateFormateado = bitrateStr != null ? bitrateStr + " kbps" : "N/A";
            String tituloFormateado = tituloStr != null ? tituloStr : "N/A";

            if(bitrate==null && Fecha==null && titulo==null){
                modificarMetadatos(nombre, tamanoKB, "--:--", formato, fechaFormateada, bitrateFormateado, tituloFormateado);
            } else {
                modificarMetadatos(nombre, tamanoKB, "--:--", formato, fechaFormateada, bitrateStr, tituloStr);
            }

        } catch (IOException | org.apache.tika.exception.TikaException | org.xml.sax.SAXException e) {
            modificarMetadatos("N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A");
        }
    }

    private static String getString(String duracionStr) {
        String duracionFormateada = "--:--";
        if (duracionStr != null) {
            try {
                long duracionMillis = Long.parseLong(duracionStr);
                long minutos = TimeUnit.MILLISECONDS.toMinutes(duracionMillis);
                long segundos = TimeUnit.MILLISECONDS.toSeconds(duracionMillis) - TimeUnit.MINUTES.toSeconds(minutos);
                duracionFormateada = String.format("%02d:%02d", minutos, segundos);
            } catch (NumberFormatException e) {
                duracionFormateada = "N/A";
            }
        }
        return duracionFormateada;
    }

    public void TratarArchivo(int i , Object t ) {
        switch (i) {
            case 1:
                // Cambiar de nombre fichero: pedir nombre al usuario
                String nuevoNombre = JOptionPane.showInputDialog(null, "Introduce el nuevo nombre del archivo:");
                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    File file = new File(archivoVideo.getParent(), nuevoNombre);
                    if (!archivoVideo.renameTo(file)) {
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


                    JTextField fechaYhoraField = new JTextField();
                    JTextField bitrate = new JTextField();
                    JTextField titulo = new JTextField();

                    panel.add(new JLabel("Fecha y hora (YYYY:MM:DD HH:MM:SS):"));
                    panel.add(fechaYhoraField);
                    panel.add(new JLabel("Bitrate:"));
                    panel.add(bitrate);
                    panel.add(new JLabel("Titulo:"));
                    panel.add(titulo);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Modificar Metadatos",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result != JOptionPane.OK_OPTION) {
                        JOptionPane.showMessageDialog(null, "Operación cancelada.");
                        break;
                    }

                    String fechaYhora = fechaYhoraField.getText().trim();
                    String velObt = bitrate.getText().trim();
                    String modelo = titulo.getText().trim();

                    if (fechaYhora.isEmpty() || velObt.isEmpty() || modelo.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Todos los campos deben estar completos.");
                        break;
                    }
                    obtenerMetadatos(archivoVideo,velObt, fechaYhora, modelo);
                    modificarYGuardarMetadatos(archivoVideo, fechaYhora, velObt, modelo);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al modificar los metadatos: " + e.getMessage());
                }
                break;
        }
    }

    private void modificarYGuardarMetadatos(File videoFile, String fechaYhora, String bitrate, String titulo) {
        try {
            // Archivo de salida temporal
            File archivoModificado = new File(videoFile.getParent(), "modificado_" + videoFile.getName());
            if (archivoModificado.exists()) {
                archivoModificado.delete();
            }

            // Construir el comando FFmpeg
            // Solo se añaden metadatos válidos
            String comando = String.format(
                "ffmpeg -y -i \"%s\" -metadata creation_time=\"%s\" -metadata title=\"%s\" -c copy \"%s\"",
                videoFile.getAbsolutePath(),
                fechaYhora,
                titulo,
                archivoModificado.getAbsolutePath()
            );

            // Ejecutar el comando
            Process proceso = Runtime.getRuntime().exec(new String[] { "bash", "-c", comando });

            // Leer la salida del proceso
            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getErrorStream()));
            String linea;
            StringBuilder errorMsg = new StringBuilder();
            while ((linea = reader.readLine()) != null) {
                errorMsg.append(linea).append("\n");
            }

            int exitCode = proceso.waitFor();
            if (exitCode == 0) {
                JOptionPane.showMessageDialog(null, "Metadatos modificados y guardados correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al modificar los metadatos. Código de salida: " + exitCode + "\n" + errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al modificar los metadatos: " + e.getMessage());
        }
    }
    public File getArchivo(){
        return archivoVideo;
    }

}