package es.ujaen.sistemasmultimedia.MULTIMEDIA;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.imageio.ImageIO;
import com.mpatric.mp3agic.*;
import es.ujaen.sistemasmultimedia.INTERFAZ.UI;
import es.ujaen.sistemasmultimedia.METADATOS_ONLINE.DiscogsSearcher;

import org.json.JSONObject;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.Equalizer;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

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
        i.abiertomusica=true;
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
            ImageIcon icon = new ImageIcon("imagenes/" + iconName);
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
            BufferedImage genericImg = ImageIO.read(new FileInputStream("imagenes/caratuladefault.png"));
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
        // Panel para solicitar título y artista en una sola ventana
        JPanel panelEntrada = new JPanel(new GridLayout(0, 2, 5, 5)); // 0 filas (se ajusta), 2 columnas, gaps
        JTextField campoTituloBusqueda = new JTextField(25);
        JTextField campoArtistaBusqueda = new JTextField(25);

        panelEntrada.add(new JLabel("Título de la canción:"));
        panelEntrada.add(campoTituloBusqueda);
        panelEntrada.add(new JLabel("Artista (opcional):"));
        panelEntrada.add(campoArtistaBusqueda);

        int resultadoDialogo = JOptionPane.showConfirmDialog(null, panelEntrada,
                "Buscar Metadatos Online en Discogs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultadoDialogo == JOptionPane.OK_OPTION) {
            String tituloCancion = campoTituloBusqueda.getText();
            String artistaCancion = campoArtistaBusqueda.getText();

            if (tituloCancion == null || tituloCancion.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El título de la canción no puede estar vacío.", "Entrada inválida", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // artistaCancion puede ser vacío, DiscogsSearcher lo maneja.

            try {
                JSONObject datos = DiscogsSearcher.buscarEnDiscogs(tituloCancion, artistaCancion);
                if (datos == null) {
                    Object[] options = {"Cancelar", "Introducir Metadatos Manualmente", "Importar Metadatos Online"};
                    int choice = JOptionPane.showOptionDialog(null,
                            "No se encontraron datos online para '" + tituloCancion + (artistaCancion.isEmpty() ? "" : " - " + artistaCancion) + "'.\n¿Qué desea hacer?",
                            "Búsqueda en Discogs Fallida",
                            JOptionPane.DEFAULT_OPTION, // Usar DEFAULT_OPTION para botones personalizados
                            JOptionPane.QUESTION_MESSAGE,
                            null,    // Sin icono personalizado
                            options, // Los textos de los botones
                            options[0]); // Botón "Cancelar" seleccionado por defecto

                    if (choice == 1) { // "Introducir Metadatos Manualmente"
                        mostrarFormularioManual(archivo);
                    } else if (choice == 2) { // "Importar Metadatos Online"
                        modificarMETADATOSONLINE(archivo); // Llama recursivamente para reintentar
                    }
                    // Si choice == 0 ("Cancelar") o JOptionPane.CLOSED_OPTION, simplemente retorna.
                    return; // Es importante salir del método aquí.
                }


                // Extraer campos del JSON devuelto por DiscogsSearcher
                String titulo = datos.optString("titulo");
                String artista = datos.optString("artista");
                String album = datos.optString("album");
                String genero = datos.optString("genero");
                String pista = datos.optString("pista");
                String caratulaURL = datos.optString("caratula");

                // Panel principal para el diálogo de edición
                JPanel panelEdicion = new JPanel(new BorderLayout(15, 10)); // Gap entre componentes
                panelEdicion.setBorder(new EmptyBorder(10, 10, 10, 10)); // Margen exterior

                // Panel izquierdo para los campos de metadatos (usando GridBagLayout)
                JPanel metadataFieldsPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbcMeta = new GridBagConstraints();
                gbcMeta.insets = new Insets(3, 3, 3, 3); // Espaciado entre componentes
                gbcMeta.anchor = GridBagConstraints.WEST; // Alinear etiquetas a la izquierda

                JTextField campoTituloEdit = new JTextField(titulo, 20); // Ancho sugerido
                JTextField campoArtistaEdit = new JTextField(artista, 20);
                JTextField campoAlbumEdit = new JTextField(album, 20);
                JTextField campoGeneroEdit = new JTextField(genero, 20);
                JTextField campoPistaEdit = new JTextField(pista, 20);

                // Fila 0: Título
                gbcMeta.gridx = 0; gbcMeta.gridy = 0;
                metadataFieldsPanel.add(new JLabel("Titulo:"), gbcMeta);
                gbcMeta.gridx = 1; gbcMeta.fill = GridBagConstraints.HORIZONTAL; gbcMeta.weightx = 1.0;
                metadataFieldsPanel.add(campoTituloEdit, gbcMeta);
                gbcMeta.fill = GridBagConstraints.NONE; gbcMeta.weightx = 0; // Reset

                // Fila 1: Artista
                gbcMeta.gridx = 0; gbcMeta.gridy = 1;
                metadataFieldsPanel.add(new JLabel("Artista:"), gbcMeta);
                gbcMeta.gridx = 1; gbcMeta.fill = GridBagConstraints.HORIZONTAL; gbcMeta.weightx = 1.0;
                metadataFieldsPanel.add(campoArtistaEdit, gbcMeta);
                gbcMeta.fill = GridBagConstraints.NONE; gbcMeta.weightx = 0;

                // Fila 2: Álbum
                gbcMeta.gridx = 0; gbcMeta.gridy = 2;
                metadataFieldsPanel.add(new JLabel("Álbum:"), gbcMeta);
                gbcMeta.gridx = 1; gbcMeta.fill = GridBagConstraints.HORIZONTAL; gbcMeta.weightx = 1.0;
                metadataFieldsPanel.add(campoAlbumEdit, gbcMeta);
                gbcMeta.fill = GridBagConstraints.NONE; gbcMeta.weightx = 0;

                // Fila 3: Género
                gbcMeta.gridx = 0; gbcMeta.gridy = 3;
                metadataFieldsPanel.add(new JLabel("Género:"), gbcMeta);
                gbcMeta.gridx = 1; gbcMeta.fill = GridBagConstraints.HORIZONTAL; gbcMeta.weightx = 1.0;
                metadataFieldsPanel.add(campoGeneroEdit, gbcMeta);
                gbcMeta.fill = GridBagConstraints.NONE; gbcMeta.weightx = 0;

                // Fila 4: Pista
                gbcMeta.gridx = 0; gbcMeta.gridy = 4;
                metadataFieldsPanel.add(new JLabel("Pista:"), gbcMeta);
                gbcMeta.gridx = 1; gbcMeta.fill = GridBagConstraints.HORIZONTAL; gbcMeta.weightx = 1.0;
                metadataFieldsPanel.add(campoPistaEdit, gbcMeta);
                gbcMeta.fill = GridBagConstraints.NONE; gbcMeta.weightx = 0;
                
                // Panel derecho para la carátula
                JPanel coverPanel = new JPanel(new BorderLayout(5,5)); // Gap vertical entre título y carátula

                JLabel caratulaDisplayLabel = new JLabel();
                caratulaDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
                caratulaDisplayLabel.setVerticalAlignment(SwingConstants.CENTER);
                caratulaDisplayLabel.setPreferredSize(new Dimension(150, 150)); // Tamaño deseado
                caratulaDisplayLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Borde opcional

                if (caratulaURL != null && !caratulaURL.isEmpty()) {
                    try {
                        URL url = new URL(caratulaURL);
                        BufferedImage img = ImageIO.read(url);
                        if (img != null) {
                            Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                            caratulaDisplayLabel.setIcon(new ImageIcon(scaledImg));
                        } else {
                            caratulaDisplayLabel.setText("<html><center>Sin carátula<br>disponible</center></html>");
                        }
                    } catch (IOException ex) {
                        caratulaDisplayLabel.setText("<html><center>Error al<br>cargar carátula</center></html>");
                    }
                } else {
                    caratulaDisplayLabel.setText("<html><center>Sin carátula<br>disponible</center></html>");
                }
                
                JLabel coverTitleLabel = new JLabel("Carátula:");
                coverTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                coverPanel.add(coverTitleLabel, BorderLayout.NORTH);
                coverPanel.add(caratulaDisplayLabel, BorderLayout.CENTER);

                // Añadir paneles al panel principal del diálogo
                panelEdicion.add(metadataFieldsPanel, BorderLayout.CENTER); // Los campos de texto se expandirán
                panelEdicion.add(coverPanel, BorderLayout.EAST);    // La carátula a la derecha

                Object[] optiones = {"Cancelar", "Introducir Metadatos Manualmente", "Importar Metadatos Online"};
                int opcionEdicion = JOptionPane.showOptionDialog(null,
                        panelEdicion,
                        "Confirmar Metadatos",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        optiones,
                        optiones[1]);

                if (opcionEdicion == 1) { // "Introducir Metadatos Manualmente"
                    mostrarFormularioManual(archivo);
                }

                if (opcionEdicion == 2) {
                    String finalTitulo = campoTituloEdit.getText();
                    String finalArtista = campoArtistaEdit.getText();
                    String finalAlbum = campoAlbumEdit.getText();
                    String finalGenero = campoGeneroEdit.getText();
                    String finalPista = campoPistaEdit.getText();

                    modificarMetadatosArchivo(archivo, finalTitulo, finalArtista, finalAlbum, finalGenero, finalPista, caratulaURL);
                    JOptionPane.showMessageDialog(null, "¡Metadatos modificados correctamente!");
                    // Actualizar la UI si es necesario
                    mostrarMetadatos(archivo);
                    mostrarCaratula(archivo);
                }

                if(opcionEdicion==0){
                    return; // Si se selecciona "Cancelar", simplemente retorna sin hacer nada
                }
            } catch (Exception e) {
                e.printStackTrace(); // Es bueno mantener el log del error

                Object[] options = {"Cancelar", "Introducir Metadatos Manualmente", "Importar Metadatos Online"};
                String errorMessage = "Ocurrió un error durante la búsqueda online: " + e.getMessage() + "\n¿Qué desea hacer?";

                int choice = JOptionPane.showOptionDialog(null,
                        errorMessage,
                        "Error en Búsqueda Online",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        options[0]); // "Cancelar" por defecto

                if (choice == 1) { // "Introducir Metadatos Manualmente"
                    mostrarFormularioManual(archivo);
                } else if (choice == 2) { // "Importar Metadatos Online"
                    modificarMETADATOSONLINE(archivo); // Llama recursivamente para reintentar
                }
                // Si choice == 0 ("Cancelar") o JOptionPane.CLOSED_OPTION, simplemente retorna.
                return; // Salir del método después de manejar el error.
            }
        } else {
            JOptionPane.showMessageDialog(null, "Búsqueda de metadatos cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarFormularioManual(File archivo) {
    JPanel panelManual = new JPanel(new GridLayout(0, 2, 5, 5));
    JTextField campoTitulo = new JTextField();
    JTextField campoArtista = new JTextField();
    JTextField campoAlbum = new JTextField();
    JTextField campoGenero = new JTextField();
    JTextField campoPista = new JTextField();

    panelManual.add(new JLabel("Título:")); panelManual.add(campoTitulo);
    panelManual.add(new JLabel("Artista:")); panelManual.add(campoArtista);
    panelManual.add(new JLabel("Álbum:")); panelManual.add(campoAlbum);
    panelManual.add(new JLabel("Género:")); panelManual.add(campoGenero);
    panelManual.add(new JLabel("Pista:")); panelManual.add(campoPista);

    int res = JOptionPane.showConfirmDialog(null, panelManual, "Metadatos manuales",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (res == JOptionPane.OK_OPTION) {
        modificarMetadatosArchivo(archivo,
                campoTitulo.getText(),
                campoArtista.getText(),
                campoAlbum.getText(),
                campoGenero.getText(),
                campoPista.getText(),
                null); // sin carátula
        mostrarMetadatos(archivo);
        mostrarCaratula(archivo);
        JOptionPane.showMessageDialog(null, "¡Metadatos añadidos manualmente!");
    }
}


    public void modificarMetadatosArchivo(File archivo, String titulo, String artista, String album, String genero, String pista, String caratulaURL) {
        String originalFilePath = archivo.getAbsolutePath();
        String tempFilePath = originalFilePath + ".tmp";

        try {
            Mp3File mp3file = new Mp3File(archivo);
            ID3v2 id3v2Tag;

            if (mp3file.hasId3v2Tag()) {
                id3v2Tag = mp3file.getId3v2Tag();
            } else {
                // Puedes elegir ID3v23Tag o ID3v24Tag según prefieras
                id3v2Tag = new ID3v24Tag();
                mp3file.setId3v2Tag(id3v2Tag);
            }

            // Establecer metadatos de texto
            id3v2Tag.setTitle(titulo);
            id3v2Tag.setArtist(artista);
            id3v2Tag.setAlbum(album);
            id3v2Tag.setGenreDescription(genero); // mp3agic usa descripción para el género textual
            id3v2Tag.setTrack(pista);
            // mp3agic no tiene un campo directo para "Año" en ID3v2 de forma tan simple como jaudiotagger.
            // Podrías usar id3v2Tag.setYear(anio) si el año es parte de los parámetros.

            // Manejar la carátula
            if (caratulaURL != null && !caratulaURL.isEmpty()) {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(caratulaURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent", "MultiStudio/1.0"); // Buena práctica
                    String mimeType = connection.getContentType();

                    // Si no se obtiene, intentar inferir de la URL
                    if (mimeType == null || mimeType.equals("application/octet-stream") || mimeType.equals("image/unknown")) {
                        if (caratulaURL.toLowerCase().endsWith(".jpg") || caratulaURL.toLowerCase().endsWith(".jpeg")) {
                            mimeType = "image/jpeg";
                        } else if (caratulaURL.toLowerCase().endsWith(".png")) {
                            mimeType = "image/png";
                        } else {
                            mimeType = "image/jpeg"; // Default o manejar error si es crítico
                        }
                    }

                    try (InputStream is = connection.getInputStream();
                         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, bytesRead);
                        }
                        byte[] imageData = baos.toByteArray();
                        id3v2Tag.setAlbumImage(imageData, mimeType);
                    }
                } catch (IOException e) {
                    System.err.println("Error al descargar o procesar la carátula: " + e.getMessage());
                    // Opcional: mostrar un JOptionPane al usuario
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } else {
                 // Opcional: si no hay URL de carátula, podrías borrar la existente
                 // id3v2Tag.clearAlbumImage();
            }

            mp3file.save(tempFilePath); // Guardar en un archivo temporal

            // Reemplazar el archivo original con el temporal de forma segura
            File originalFile = new File(originalFilePath);
            File tempFile = new File(tempFilePath);

            // Aquí sería ideal detener la reproducción si 'archivo' es el que se está reproduciendo
            // para evitar problemas de bloqueo de archivo, pero por simplicidad lo omitimos.
            // El usuario podría necesitar recargar la canción.

            if (originalFile.delete()) {
                if (!tempFile.renameTo(originalFile)) {
                    System.err.println("Error al renombrar el archivo temporal: " + tempFile.getName() + " a " + originalFile.getName());
                    // Intentar restaurar el temporal si es posible o loguear
                    tempFile.delete(); // Limpiar el temporal si el renombrado falla
                    JOptionPane.showMessageDialog(null, "Error al guardar los cambios en el archivo original (renombrado falló).", "Error de guardado", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("Metadatos guardados correctamente en: " + originalFile.getName());
                }
            } else {
                System.err.println("Error al borrar el archivo original: " + originalFile.getName());
                tempFile.delete(); // Limpiar el temporal si el original no se pudo borrar
                JOptionPane.showMessageDialog(null, "Error al guardar los cambios (no se pudo borrar el original).", "Error de guardado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException | NotSupportedException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al modificar los metadatos con mp3agic: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // Asegurarse de que el archivo temporal se borre si existe y hubo un error antes del renombrado
            File tempFileOnError = new File(tempFilePath);
            if (tempFileOnError.exists()) {
                tempFileOnError.delete();
            }
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
