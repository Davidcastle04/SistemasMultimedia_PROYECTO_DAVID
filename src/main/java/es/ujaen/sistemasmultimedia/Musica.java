package es.ujaen.sistemasmultimedia;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;



class Musica {

    private final DefaultListModel<File> listModel = new DefaultListModel<>();
    private final JList<File> jList1 = new JList<>(listModel);
    JScrollPane jScrollPane1 = new JScrollPane();
    Label GeneroMusica = new Label("Género: ");
    Label AlbumMusica = new Label("Álbum: ");
    Label AnioMusica = new Label("Año: ");
    Label ArtistaMusica = new Label("Artista: ");
    Label PistaMusica = new Label("Número de pista:");
    Label NombreMusica = new Label("Nombre: ");
    Label IndiceMusica = new Label("--:-- / --:--");
    JTextField Estado = null;
    MediaPlayer mediaPlayer;
    //CaratulaPanel caratulaPanel = new CaratulaPanel();
    JSlider jSlider1 = new JSlider(0, 100, 0);
    public void addMusica(File archivo) {
        listModel.addElement(archivo);
        CambiarAudio(archivo);
    }

    private void CambiarAudio(File archivo) {
        Estado.setText("AUDIO " + archivo.getName());
        mediaPlayer.media().play(archivo.getAbsolutePath());
        jSlider1.setValue(0);
        //SwingUtilities.invokeLater(() -> caratulaPanel.setCaratula(archivo));

       //Set Metadata
       mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
           public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
               // Obtener los metadatos individualmente
               String title = mediaPlayer.media().meta().get(Meta.TITLE);
               String genre = mediaPlayer.media().meta().get(Meta.GENRE);
               String album = mediaPlayer.media().meta().get(Meta.ALBUM);
               String artist = mediaPlayer.media().meta().get(Meta.ARTIST);
               String date = mediaPlayer.media().meta().get(Meta.DATE);
               String track = mediaPlayer.media().meta().get(Meta.TRACK_NUMBER);

               // Actualizar las etiquetas con los valores obtenidos
               NombreMusica.setText("Nombre: " + (title != null ? title : "Desconocido"));
               GeneroMusica.setText("Género: " + (genre != null ? genre : "Desconocido"));
               AlbumMusica.setText("Álbum: " + (album != null ? album : "Desconocido"));
               ArtistaMusica.setText("Artista: " + (artist != null ? artist : "Desconocido"));
               AnioMusica.setText("Año: " + (date != null ? date : "Desconocido"));
               PistaMusica.setText("Pista: " + (track != null ? track : "Desconocido"));
           }
       });
    }



    JPanel Musica(File archivoSeleccionado, JTextField estado) {
        JPanel panelAudio = new JPanel();
        Estado = estado;

        // Crear el reproductor de medios
        MediaPlayerFactory factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        // Botones de control de reproducción
        RoundedReliefButton retrocesoMusica = new RoundedReliefButton("RETROCESO");
        RoundedReliefButton playMusica = new RoundedReliefButton("PAUSE/PLAY");
        RoundedReliefButton avanzarMusica = new RoundedReliefButton("AVANZAR");
        RoundedReliefButton AddPlaylistMusica = new RoundedReliefButton("Añadir Canción a la PlayList");
        RoundedReliefButton AjustarVolumenMusica = new RoundedReliefButton("Ajustar Volumen");
        RoundedReliefButton ModificarColaMusica = new RoundedReliefButton("       Modificar la cola      ");

        // Etiquetas
        Label colaDeReproduccion = new Label("Cola de Reproducción");

        // Acciones para los botones
        retrocesoMusica.addActionListener(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.controls().skipTime(-10000); // Retrocede 10 segundos
            }
        });

        playMusica.setButtonColors(new Color(70, 130, 180), Color.WHITE);
        playMusica.addActionListener(e -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.status().isPlaying()) {
                    mediaPlayer.controls().pause();
                } else {
                    mediaPlayer.controls().play();
                }
            }
        });

        avanzarMusica.addActionListener(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.controls().skipTime(10000); // Avanza 10 segundos
            }
        });

        AjustarVolumenMusica.addActionListener(e -> {
            if (mediaPlayer != null) {
                // Lógica para ajustar el volumen
            }
        });

        AddPlaylistMusica.addActionListener(this::ActionPerformacePlayListMusica);
        ModificarColaMusica.addActionListener(this::ActionPerformaceModCola);

        // Listener para actualizar la barra de progreso y el tiempo
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                long totalDuration = mediaPlayer.status().length();
                IndiceMusica.setText(formatTime(newTime) + " / " + formatTime(totalDuration));
                jSlider1.setValue((int) ((newTime * 100) / totalDuration));
            }
        });

        // Listener del slider para mover el tiempo
        jSlider1.addChangeListener(e -> {
            if (!jSlider1.getValueIsAdjusting() && mediaPlayer != null) {
                long position = (long) ((jSlider1.getValue() / 100.0) * mediaPlayer.status().length());
                mediaPlayer.controls().setTime(position);
            }
        });

        // Añadir canción al reproductor y lista
        addMusica(archivoSeleccionado);
        jScrollPane1.setViewportView(jList1);

        // Layout del panel
        GroupLayout layout = new GroupLayout(panelAudio);
        panelAudio.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(39)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(PistaMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(AnioMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ArtistaMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(200)
                                                .addComponent(IndiceMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(30)
                                                .addComponent(retrocesoMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(25)
                                                .addComponent(playMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(27)
                                                .addComponent(avanzarMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(NombreMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(GeneroMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(AlbumMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(50, 50, 50)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(AddPlaylistMusica, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(AjustarVolumenMusica, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ModificarColaMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(50)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(colaDeReproduccion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jSlider1, GroupLayout.PREFERRED_SIZE, 417, GroupLayout.PREFERRED_SIZE)
                                                .addGap(96))))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(colaDeReproduccion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(AddPlaylistMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(AjustarVolumenMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ModificarColaMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(NombreMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(GeneroMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(AlbumMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSlider1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(AnioMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ArtistaMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addComponent(PistaMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)

                                        .addComponent(IndiceMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(19)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(playMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(retrocesoMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(avanzarMusica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap())
        );

        return panelAudio;
    }


    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void ActionPerformacePlayListMusica(java.awt.event.ActionEvent evt) {
        System.out.println("Implementar lógica para añadir a la playlist");
    }

    private void ActionPerformaceModCola(java.awt.event.ActionEvent evt) {
        System.out.println("Implementar lógica para modificar la cola");
    }
}

class CaratulaPanel extends JPanel {

    private BufferedImage image;
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 200;

    public CaratulaPanel() {
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    public void setCaratula(File file) {
        // Load image in a background thread
        new SwingWorker<BufferedImage, Void>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                BufferedImage loadedImage = loadImageFromFile(file);
                if (loadedImage == null) {
                    // Load default image if the file doesn't have one
                    loadedImage = getDefaultImage();
                }
                return loadedImage;
            }

            @Override
            protected void done() {
                try {
                    image = get();
                    repaint();  // Trigger a repaint to display the new image
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.printf("Usando caratula por defecto ");
                    image = getDefaultImage();
                    repaint();
                }
            }
        }.execute();
    }

    private BufferedImage loadImageFromFile(File file) {
        try {
            // Attempt to read the image file
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage getDefaultImage() {
        try {
            // Load a default image from resources
            return ImageIO.read(getClass().getResource("imagenes/default.jpeg")); // Ensure default_artwork.png exists in your resources
        } catch (IOException e) {
            System.out.printf("Error loading default image: %s%n", e.getMessage());
            return null; // Handle the error appropriately
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Scale the image to fit the panel
            int width = getWidth();
            int height = getHeight();
            g.drawImage(image, 0, 0, width, height, this);
        } else {
            // Draw a placeholder if no image is available
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

