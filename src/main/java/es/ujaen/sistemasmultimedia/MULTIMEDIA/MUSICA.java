package es.ujaen.sistemasmultimedia.MULTIMEDIA;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import com.mpatric.mp3agic.*;
import es.ujaen.sistemasmultimedia.INTERFAZ.UI;
import es.ujaen.sistemasmultimedia.METADATOS_ONLINE.DiscogsSearcher;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.json.JSONObject;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.Equalizer;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MUSICA {

    private MediaPlayerFactory mediaPlayerFactory;
    private MediaPlayer mediaPlayer;
    private Equalizer ecualizador;

    private JPanel panelAudio;
    private JScrollPane jScrollPane1;
    private File actual;

    private JButton retrocesoMusica;
    private JButton playMusica;
    private JButton avanzarMusica;
    private JButton AddPlaylistMusica;
    private JButton favoritoMusica; // Añade este atributo

    private JLabel GeneroMusica;
    private JLabel AlbumMusica;
    private JLabel AnioMusica;
    private JLabel ArtistaMusica;
    private JLabel PistaMusica;
    private JLabel colaDeReproduccion;
    private JLabel IndiceMusica;
    private JLabel NombreMusica;
    private JList<File> jList1;
    private UI interfaz; // Referencia a la interfaz principal
    private JSlider jSlider1; // Slider tiempo canción
    private JSlider volumenSlider; // Slider volumen

    private JPanel panelCaratula; // Para la carátula
    private JPanel panelEcualizador;

    private DefaultListModel<File> playlistModel;

    private Timer timer; // Para actualizar el slider y el tiempo
    private List<VIDEO> videosAbiertos;

    private static final String[] BAND_LABELS = {
            "60Hz", "170Hz", "310Hz", "600Hz", "1KHz",
            "3KHz", "6KHz", "12KHz", "14KHz", "16KHz"
    };

    public MUSICA(List<VIDEO> videosAbiertos , UI i ) {
        this.videosAbiertos = videosAbiertos;
        interfaz = i;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        panelAudio = new JPanel();
        jScrollPane1 = new JScrollPane();
        retrocesoMusica = new JButton("RETROCESO");
        playMusica = new JButton("PAUSA/PLAY");
        avanzarMusica = new JButton("ADELANTAR");
        AddPlaylistMusica = new JButton("Añadir a Playlist");
        favoritoMusica = new JButton("Favorito");
        setButtonIcon(favoritoMusica, "favorito.png");
        setButtonIcon(retrocesoMusica, "retroceso.png");
        setButtonIcon(playMusica, "pauseplay.png");
        setButtonIcon(avanzarMusica, "avanzar.png");
        setButtonIcon(AddPlaylistMusica, "add.png");

        GeneroMusica = new JLabel("Género: ");
        AlbumMusica = new JLabel("Álbum: ");
        AnioMusica = new JLabel("Año: ");
        ArtistaMusica = new JLabel("Artista: ");
        PistaMusica = new JLabel("Pista: ");
        colaDeReproduccion = new JLabel("Cola de Reproducción");
        IndiceMusica = new JLabel("--:-- / --:--");
        NombreMusica = new JLabel("Nombre: ");
        jList1 = new JList<>();
        playlistModel = new DefaultListModel<>();
        jList1.setModel(playlistModel);

        // Personaliza cómo se muestra cada elemento
        jList1.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof File) {
                    value = ((File) value).getName();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        jSlider1 = new JSlider();
        volumenSlider = new JSlider(0, 100, 100); // volumen inicial 100%
        volumenSlider.setPreferredSize(new Dimension(100, 20));
        volumenSlider.setToolTipText("Volumen");

        panelCaratula = new JPanel();
        panelEcualizador = new JPanel();

        panelCaratula.setPreferredSize(new Dimension(180, 180));
        panelCaratula.setBackground(Color.WHITE);
        panelCaratula.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2, true),
            new EmptyBorder(10, 10, 10, 10) // margen interior
        ));
    }

    public JPanel AUDIO(File archivoSeleccionado, JTextField estado) {
        // Pausar todos los vídeos abiertos
        if (videosAbiertos != null) {
            for (VIDEO v : videosAbiertos) {
                if (v.getArchivo() != null && v.mediaPlayerComponent != null) {
                    v.mediaPlayerComponent.mediaPlayer().controls().pause();
                }
            }
        }

        // Inicializar MediaPlayer y Equalizer si no existen
        if (mediaPlayer == null) {
            mediaPlayerFactory = new MediaPlayerFactory();
            mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
            ecualizador = mediaPlayerFactory.equalizer().newEqualizer();
            mediaPlayer.audio().setEqualizer(ecualizador);
            mediaPlayer.audio().setVolume(100);
            mediaPlayer.audio().setMute(false);

            mediaPlayer.events().addMediaPlayerEventListener(new uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter() {
                @Override
                public void finished(uk.co.caprica.vlcj.player.base.MediaPlayer mp) {
                    SwingUtilities.invokeLater(() -> reproducirSiguiente());
                }
            });
        }

        // Detener reproducción anterior
        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().stop();
        }

        mediaPlayer.media().start(archivoSeleccionado.getAbsolutePath());

        estado.setText("AUDIO " + archivoSeleccionado.getName());
        actual = archivoSeleccionado;
        jScrollPane1.setViewportView(jList1);

        mostrarCaratula(archivoSeleccionado);
        mostrarMetadatos(archivoSeleccionado);

        // Ecualizador visual con etiquetas
        panelEcualizador.removeAll();
        panelEcualizador.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; gbc.insets = new Insets(2, 8, 2, 8);
        for (int i = 0; i < 10; i++) {
            JPanel sliderPanel = new JPanel(new BorderLayout());
            JSlider slider = new JSlider(JSlider.VERTICAL, -20, 20, 0);
            slider.setPreferredSize(new Dimension(40, 100));
            int band = i;
            slider.addChangeListener(e -> {
                ecualizador.setAmp(band, slider.getValue());
                mediaPlayer.audio().setEqualizer(ecualizador); // Aplica el ecualizador actualizado
            });
            JLabel label = new JLabel(BAND_LABELS[i], SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 11));
            sliderPanel.add(slider, BorderLayout.CENTER);
            sliderPanel.add(label, BorderLayout.SOUTH);
            gbc.gridx = i;
            panelEcualizador.add(sliderPanel, gbc);
        }

        // Slider de tiempo
        jSlider1.setMinimum(0);
        jSlider1.setMaximum(1000);
        jSlider1.setValue(0);
        jSlider1.addChangeListener(e -> {
            if (jSlider1.getValueIsAdjusting() && mediaPlayer.status().isPlaying()) {
                float position = jSlider1.getValue() / 1000.0f;
                mediaPlayer.controls().setPosition(position);
            }
        });

        // Slider volumen
        volumenSlider.addChangeListener(e -> {
            int vol = volumenSlider.getValue();
            mediaPlayer.audio().setVolume(vol);
        });

        // Timer para actualizar el slider y el tiempo
        if (timer != null) timer.stop();
        timer = new Timer(500, e -> {
            long duracion = mediaPlayer.media().info().duration();
            long actualTime = mediaPlayer.status().time();
            if (duracion > 0) {
                int pos = (int) ((actualTime * 1000) / duracion);
                jSlider1.setValue(pos);
                IndiceMusica.setText(formatTime(actualTime) + " / " + formatTime(duracion));
            }
        });
        timer.start();

        // Botón añadir a playlist
        for (ActionListener al : AddPlaylistMusica.getActionListeners()) {
            AddPlaylistMusica.removeActionListener(al);
        }
        AddPlaylistMusica.addActionListener(evt -> {
            if (archivoSeleccionado != null && archivoSeleccionado.exists() && !playlistModel.contains(archivoSeleccionado)) {
                playlistModel.addElement(archivoSeleccionado);
            }
        });

        // Boton de Favoritos
        favoritoMusica.addActionListener(e -> {
            interfaz.addFavoritos(actual);
        });



        // Botón play/pause
        for (ActionListener al : playMusica.getActionListeners()) {
            playMusica.removeActionListener(al);
        }
        playMusica.addActionListener(evt -> {
            interfaz.ArchivoAbierto.setText("Musica: "+actual.getName());
            if (mediaPlayer.status().isPlaying()) {
                mediaPlayer.controls().pause();
            } else {
                if (videosAbiertos != null) {
                    for (VIDEO v : videosAbiertos) {
                        if (v.getArchivo() != null && v.mediaPlayerComponent != null) {
                            v.mediaPlayerComponent.mediaPlayer().controls().pause();
                        }
                    }
                }
                mediaPlayer.controls().play();
            }
        });

        // Botón avanzar
        for (ActionListener al : avanzarMusica.getActionListeners()) {
            avanzarMusica.removeActionListener(al);
        }
        avanzarMusica.addActionListener(evt -> reproducirSiguiente());

        // Botón retroceso
        for (ActionListener al : retrocesoMusica.getActionListeners()) {
            retrocesoMusica.removeActionListener(al);
        }
        retrocesoMusica.addActionListener(evt -> reproducirAnterior());

        // Selección de la playlist
        jList1.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    File selected = jList1.getSelectedValue();
                    if (selected != null) {
                        reproducirArchivo(selected);
                    }
                }
            }
        });

        // Layout visual mejorado
        panelAudio.removeAll();
        panelAudio.setLayout(new BorderLayout(15, 10));

        // Panel izquierdo: carátula con margen
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // margen exterior
        leftPanel.add(panelCaratula, BorderLayout.NORTH);

        // Panel central: metadatos
        JPanel metaPanel = new JPanel();
        metaPanel.setLayout(new BoxLayout(metaPanel, BoxLayout.Y_AXIS));
        metaPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        metaPanel.add(NombreMusica);
        metaPanel.add(Box.createVerticalStrut(5));
        metaPanel.add(ArtistaMusica);
        metaPanel.add(Box.createVerticalStrut(5));
        metaPanel.add(AlbumMusica);
        metaPanel.add(Box.createVerticalStrut(5));
        metaPanel.add(GeneroMusica);
        metaPanel.add(Box.createVerticalStrut(5));
        metaPanel.add(AnioMusica);
        metaPanel.add(Box.createVerticalStrut(5));
        metaPanel.add(PistaMusica);

        // Panel inferior: controles centrados
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        botones.add(favoritoMusica);
        botones.add(retrocesoMusica);
        botones.add(playMusica);
        botones.add(avanzarMusica);
        botones.add(AddPlaylistMusica);

        JPanel slidersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        slidersPanel.add(new JLabel("Tiempo:"));
        slidersPanel.add(jSlider1);
        slidersPanel.add(IndiceMusica);
        slidersPanel.add(Box.createHorizontalStrut(20));
        slidersPanel.add(new JLabel("Volumen:"));
        slidersPanel.add(volumenSlider);

        controls.add(botones);
        controls.add(slidersPanel);

        // Panel derecho: playlist con margen
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(180, 250));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // margen exterior
        colaDeReproduccion.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(colaDeReproduccion, BorderLayout.NORTH);
        rightPanel.add(jScrollPane1, BorderLayout.CENTER);

        // Ecualizador arriba, centrado
        JPanel eqWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        eqWrapper.add(panelEcualizador);

        panelAudio.add(eqWrapper, BorderLayout.NORTH);
        panelAudio.add(leftPanel, BorderLayout.WEST);
        panelAudio.add(metaPanel, BorderLayout.CENTER);
        panelAudio.add(controls, BorderLayout.SOUTH);
        panelAudio.add(rightPanel, BorderLayout.EAST);

        panelAudio.revalidate();
        panelAudio.repaint();

        AddPlaylistMusica.addActionListener(e->{
            PlayList();
        });

        return panelAudio;
    }
    private void mostrarCaratula(File archivo) {
        panelCaratula.removeAll();
        panelCaratula.setLayout(new GridBagLayout()); 
        try {
            Mp3File mp3file = new Mp3File(archivo);
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                byte[] imageData = id3v2Tag.getAlbumImage();
                if (imageData != null) {
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
                    if (img != null) {
                        JLabel picLabel = new JLabel(new ImageIcon(img.getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
                        panelCaratula.add(picLabel, new GridBagConstraints());
                    } else {
                        mostrarImagenGenericaOCartel();
                    }
                } else {
                    mostrarImagenGenericaOCartel();
                }
            } else {
                mostrarImagenGenericaOCartel();
            }
        } catch (Exception e) {
            mostrarImagenGenericaOCartel();
        }
        panelCaratula.revalidate();
        panelCaratula.repaint();
    }

    private void setButtonIcon(JButton button, String iconName) {
        try {
            String path = System.getProperty("user.dir") + "/imagenes/" + iconName;
            ImageIcon icon = new ImageIcon(path);
            button.setIcon(new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
        } catch (Exception e) {
            // Si falla, deja solo el texto
        }
    }

    // Intenta mostrar una imagen genérica, si no puede, muestra el texto "Sin carátula"
    private void mostrarImagenGenericaOCartel() {
        panelCaratula.setLayout(new GridBagLayout());
        try {
            String path = System.getProperty("user.dir") + "/imagenes/caratuladefault.png";
            BufferedImage genericImg = ImageIO.read(new FileInputStream(path));
            if (genericImg != null) {
                JLabel picLabel = new JLabel(new ImageIcon(genericImg.getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
                panelCaratula.add(picLabel, new GridBagConstraints());
            } else {
                panelCaratula.add(new JLabel("Sin carátula"), new GridBagConstraints());
            }
        } catch (Exception ex) {
            panelCaratula.add(new JLabel("Sin carátula"), new GridBagConstraints());
        }
    }

    private void mostrarMetadatos(File archivo) {
        try {
            Mp3File mp3file = new Mp3File(archivo);
            String nombre = archivo.getName();
            String artista = "", album = "", genero = "", anio = "", pista = "";
            if (mp3file.hasId3v2Tag()) {
                ID3v2 tag = mp3file.getId3v2Tag();
                artista = tag.getArtist() != null ? tag.getArtist() : "";
                album = tag.getAlbum() != null ? tag.getAlbum() : "";
                genero = tag.getGenreDescription() != null ? tag.getGenreDescription() : "";
                anio = tag.getYear() != null ? tag.getYear() : "";
                pista = tag.getTrack() != null ? tag.getTrack() : "";
            }
            NombreMusica.setText("Nombre: " + nombre);
            ArtistaMusica.setText("Artista: " + artista);
            AlbumMusica.setText("Álbum: " + album);
            GeneroMusica.setText("Género: " + genero);
            AnioMusica.setText("Año: " + anio);
            PistaMusica.setText("Pista: " + pista);
        } catch (Exception e) {
            NombreMusica.setText("Nombre: " + archivo.getName());
            ArtistaMusica.setText("Artista: -");
            AlbumMusica.setText("Álbum: -");
            GeneroMusica.setText("Género: -");
            AnioMusica.setText("Año: -");
            PistaMusica.setText("Pista: -");
        }
    }

    private void reproducirArchivo(File archivo) {
        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().stop();
        }
        mediaPlayer.media().start(archivo.getAbsolutePath());
        actual = archivo;
        interfaz.ArchivoAbierto.setText("Musica: "+archivo.getName());
        mostrarCaratula(archivo);
        mostrarMetadatos(archivo);
    }

    private void reproducirSiguiente() {
        int idx = playlistModel.indexOf(actual);
        if (idx < playlistModel.size() - 1) {
            File siguiente = playlistModel.get(idx + 1);
            jList1.setSelectedValue(siguiente, true);
            reproducirArchivo(siguiente);
        }
    }

    private void PlayList(){
        String[] opciones = {"PlayList Existente", "Nueva Playlist"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "¿Deseas guardar la canción en una playlist existente o desea crear una nueva?",
                "Añadir a playlist",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == 0) { // Álbum existente
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Selecciona el archivo .pylist del álbum");
            chooser.setFileFilter(new FileNameExtensionFilter("Ficheros .pylist", "pylist"));

            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File iniFile = chooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(iniFile, true)) {
                    writer.write(getArchivo().getAbsolutePath() + System.lineSeparator());
                    JOptionPane.showMessageDialog(null, "Musica añadida a la playlist existente.");

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al escribir en la playlist: " + e.getMessage());
                }
            }

        } else if (seleccion == 1) { // Álbum nuevo
            String nombre = JOptionPane.showInputDialog(null, "Introduce el nombre de la nueva playlist:");
            if (nombre != null && !nombre.trim().isEmpty()) {

                File iniFil = new File("../PLAYLIST/"+nombre+".pylist");
                try(FileWriter writer = new FileWriter(iniFil, true)) {
                    writer.write("NOMBRE_PLAYLIST="+nombre+System.lineSeparator());
                    writer.write(getArchivo().getAbsolutePath() + System.lineSeparator());
                    JOptionPane.showMessageDialog(null, "Música añadida a la playlist existente.");

                }catch(IOException se){
                    JOptionPane.showMessageDialog(null, "Error al escribir en la playlist: " + se.getMessage());
                }
                JOptionPane.showMessageDialog(null, "Playlist '" + nombre + "' creada y se añadido la música.");
            }
        }
    }

    private void reproducirAnterior() {
        int idx = playlistModel.indexOf(actual);
        if (idx > 0) {
            File anterior = playlistModel.get(idx - 1);
            jList1.setSelectedValue(anterior, true);
            reproducirArchivo(anterior);
        }
    }

    public File getArchivo() {
        return actual;
    }

    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void addMusica(File archivo) {
        if (archivo != null && archivo.exists() && !playlistModel.contains(archivo)) {
            playlistModel.addElement(archivo);
        }
    }

    public void modificarMETADATOSONLINE(File archivo) {
        String cancion = JOptionPane.showInputDialog(null, "Introduce el nombre de la canción:", "Buscar metadatos online", JOptionPane.PLAIN_MESSAGE);
        if (cancion == null || cancion.isEmpty()) return;

        try {
            // Llamada a Discogs
            JSONObject datos = DiscogsSearcher.buscarEnDiscogs(cancion);
            if (datos == null) {
                JOptionPane.showMessageDialog(null, "No se encontraron datos online.");
                return;
            }

            // Extraer campos
            String titulo = datos.optString("titulo");
            String artista = datos.optString("artista");
            String album = datos.optString("album");
            String genero = datos.optString("genero");
            String pista = datos.optString("pista");
            String caratulaURL = datos.optString("caratula");

            JPanel panel = new JPanel(new GridLayout(6, 2));
            JTextField campoTitulo = new JTextField(titulo);
            JTextField campoArtista = new JTextField(artista);
            JTextField campoAlbum = new JTextField(album);
            JTextField campoGenero = new JTextField(genero);
            JTextField campoPista = new JTextField(pista);
            JLabel caratulaLabel = new JLabel(caratulaURL != null ? "<html><img src='" + caratulaURL + "' width='100'></html>" : "Sin carátula");

            panel.add(new JLabel("Título:")); panel.add(campoTitulo);
            panel.add(new JLabel("Artista:")); panel.add(campoArtista);
            panel.add(new JLabel("Álbum:")); panel.add(campoAlbum);
            panel.add(new JLabel("Género:")); panel.add(campoGenero);
            panel.add(new JLabel("Pista:")); panel.add(campoPista);
            panel.add(new JLabel("Carátula:")); panel.add(caratulaLabel);

            int opcion = JOptionPane.showOptionDialog(null, panel, "Editar/Importar Metadatos",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    new String[]{"Importar metadatos online", "Editar manualmente"}, "Importar");

            if (opcion == JOptionPane.CLOSED_OPTION) return;

            // Si eliges importar: usa los datos actuales
            // Si eliges editar: usa lo que escribió el usuario

            String finalTitulo = campoTitulo.getText();
            String finalArtista = campoArtista.getText();
            String finalAlbum = campoAlbum.getText();
            String finalGenero = campoGenero.getText();
            String finalPista = campoPista.getText();

            modificarMetadatosArchivo(archivo, finalTitulo, finalArtista, finalAlbum, finalGenero, finalPista, caratulaURL);

            JOptionPane.showMessageDialog(null, "¡Metadatos modificados correctamente!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al modificar los metadatos.");
        }
    }

    public void modificarMetadatosArchivo(File archivo, String titulo, String artista, String album, String genero, String pista, String caratulaURL) {
        try {
            AudioFile f = AudioFileIO.read(archivo);
            Tag tag = f.getTagOrCreateAndSetDefault();

            tag.setField(FieldKey.TITLE, titulo);
            tag.setField(FieldKey.ARTIST, artista);
            tag.setField(FieldKey.ALBUM, album);
            tag.setField(FieldKey.GENRE, genero);
            tag.setField(FieldKey.TRACK, pista);

            // Descargar imagen desde URL y guardar en archivo temporal
            File tempImageFile = null;
            if (caratulaURL != null && !caratulaURL.isEmpty()) {
                URL url = new URL(caratulaURL);
                try (InputStream is = url.openStream()) {
                    tempImageFile = File.createTempFile("cover", ".img");
                    try (FileOutputStream fos = new FileOutputStream(tempImageFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }

                    // Crear Artwork desde el archivo temporal
                    Artwork artwork = ArtworkFactory.createArtworkFromFile(tempImageFile);

                    tag.deleteArtworkField(); // Quitar la carátula anterior si existe
                    tag.setField(artwork);    // Añadir la nueva carátula
                }
            }


            f.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void TratarArchivo(int i, Object o) {
        switch (i){
            case 1: //Modificar el nombre de la canción
                String nuevoNombre = JOptionPane.showInputDialog(null, "Introduce el nuevo nombre del archivo:");
                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    File file = new File(actual.getParent(), nuevoNombre);
                    if (!actual.renameTo(file)) {
                        JOptionPane.showMessageDialog(null, "Error al renombrar el archivo");
                    } else {
                        JOptionPane.showMessageDialog(null, "Archivo renombrado correctamente.");
                        interfaz.cambiarNombreTAB(nuevoNombre);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nombre no válido o cancelado.");
                }
                break;
            case 2: // Modificar los metadatos de la canción
                if (actual instanceof File) {
                    File archivo = (File) actual;
                    modificarMETADATOSONLINE(archivo);
                } else {
                    JOptionPane.showMessageDialog(null, "Error: El objeto no es un archivo válido.");
                }
                break;
            case 3: // Cambiar Caratula
                if (actual instanceof File) {
                    File archivo = (File) actual;
                    modificarMETADATOSONLINE(archivo);
                } else {
                    JOptionPane.showMessageDialog(null, "Error: El objeto no es un archivo válido.");
                }
                break;

        }
    }
}
