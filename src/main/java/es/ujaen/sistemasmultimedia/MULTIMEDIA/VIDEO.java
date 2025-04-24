package es.ujaen.sistemasmultimedia.MULTIMEDIA;

import es.ujaen.sistemasmultimedia.UI;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class VIDEO {
    EmbeddedMediaPlayerComponent mediaPlayerComponent;
    UI interfaz;
    File archivoVideo;
    JPanel jPanel3 = new JPanel();
    GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
    Panel panel2 = new Panel();
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
        jPanel3.setLayout(jPanel3Layout);
    }

    public JPanel getVideo(File video) {
        archivoVideo = video;

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        panel2.setLayout(new BorderLayout());
        panel2.add(mediaPlayerComponent, BorderLayout.CENTER);

        timer.setText("--:-- / --:--");

        retroceso.setLabel("button8");

        pausaANDplay.setLabel("button9");

        avanzar.setLabel("button10");

        jLabel1.setText("Volumen");
        borrarVideo.setForeground(new Color(255, 0, 102));
        borrarVideo.setText("Eliminar Video");
        addVideosFav.setText("Añadir a Videos Favoritos");
        Metadatos.setText("Metadatos");
        NombreArchivo.setText("Nombre del archivo ");
        Tamanio.setText("Tamaño ");
        Duracion.setText("Duración");
        Formato.setText("Formato ");
        Fecha.setText("Fecha de creación/modificación");
        bitrate.setText("Bitrate total");
        Titulo.setText("Titulo");
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
                                                        .addComponent(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                        .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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

        borrarVideo.addActionListener( e -> {
            BorrarVideo(archivoVideo);
        });

        addVideosFav.addActionListener( e ->{
            JOptionPane.showMessageDialog(null, "Video"+archivoVideo.getName()+"Añadido a favoritos");
            interfaz.addFavoritos(archivoVideo);
        });
        retroceso.addActionListener( e -> {
            desplazamiento(false);
        });
        avanzar.addActionListener( e -> {
            desplazamiento(true);
        });
        pausaANDplay.addActionListener( e -> {
            pausaANDPLAY();
        });
        BARRATIEMPO.addChangeListener(e -> {
            if (BARRATIEMPO.getValueIsAdjusting()) {
                float posicion = BARRATIEMPO.getValue() / 100.0f;
                mediaPlayerComponent.mediaPlayer().controls().setPosition(posicion);
            }
        });

        BARRAVOLUMEN.addChangeListener(e -> {
            mediaPlayerComponent.mediaPlayer().audio().setVolume(BARRAVOLUMEN.getValue());
        });


        mediaPlayerComponent.mediaPlayer().media().play(archivoVideo.getAbsolutePath());
        return jPanel3;
    }

    private void BorrarVideo(File f ){
       int n = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar el video?", "Eliminar video", JOptionPane.YES_NO_OPTION);
        if (f.delete() && n == 1 ) {
            System.out.println("El archivo fue eliminado con éxito");
        } else {
            System.out.println("El archivo no pudo ser eliminado");
        }
    }

    private void modificarMetadatos(String name , String tam , String Dur , String For , String Fecha , String Bit , String Tit){
        NombreArchivo.setText("Nombre del Archivo "+ name);
        Tamanio.setText("Tamaño "+tam);
        Duracion.setText("Duración "+Dur);
        this.Fecha.setText("Fecha de Creacion: "+Fecha);
        bitrate.setText("Bitrate "+Bit);
        Titulo.setText("Titulo"+Tit);
    }
    private void pausaANDPLAY() {
        if (mediaPlayerComponent.mediaPlayer().status().isPlaying()) {
            mediaPlayerComponent.mediaPlayer().controls().pause();
        } else {
            mediaPlayerComponent.mediaPlayer().controls().play();
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
        if(tr){
            mediaPlayerComponent.release();
        }else{
            // Al volver a la pestaña o querer reproducir de nuevo:
            mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
            panel2.removeAll();
            panel2.setLayout(new BorderLayout());
            panel2.add(mediaPlayerComponent, BorderLayout.CENTER);
            panel2.revalidate();
            panel2.repaint();

            // Cargar y reproducir el video
            mediaPlayerComponent.mediaPlayer().media().play(archivoVideo.getAbsolutePath());

        }

    }


}