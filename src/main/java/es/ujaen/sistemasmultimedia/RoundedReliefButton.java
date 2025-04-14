package es.ujaen.sistemasmultimedia;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedReliefButton extends JButton {
    private Color backgroundColor = new Color(70, 130, 180); // Steel Blue (valor por defecto)
    private Color foregroundColor = Color.WHITE; // Valor por defecto
    private boolean isMouseOver = false;
    private boolean isPressed = false;
    private int arcRadius = 25; // Radio más pronunciado
    private int reliefDepth = 5; // Profundidad aumentada
    private float animationProgress = 0f;
    private Icon buttonIcon = null; // Nuevo campo para el icono

    public RoundedReliefButton(String text) {
        super(text);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isMouseOver = true;
                startHoverAnimation();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                isMouseOver = false;
                startHoverAnimation();
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                isPressed = true;
                repaint();
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                isPressed = false;
                repaint();
            }
        });
    }

    private void startHoverAnimation() {
        Timer timer = new Timer(10, e -> {
            animationProgress = isMouseOver ?
                    Math.min(1, animationProgress + 0.1f) :
                    Math.max(0, animationProgress - 0.1f);
            repaint();
            if (animationProgress == 0 || animationProgress == 1) {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        int width = getWidth();
        int height = getHeight();

        // Fondo con sombra
        int shadowOffset = (int) (4 * animationProgress);
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillRoundRect(shadowOffset, shadowOffset, width - shadowOffset, height - shadowOffset, arcRadius, arcRadius);

        // Forma principal
        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                isPressed ? reliefDepth : 0,
                isPressed ? reliefDepth : 0,
                width - (isPressed ? reliefDepth : 0),
                height - (isPressed ? reliefDepth : 0),
                arcRadius,
                arcRadius
        );

        // Gradiente dinámico
        Color startColor = blendColors(backgroundColor, backgroundColor.brighter(), animationProgress);
        Color endColor = startColor.darker();
        GradientPaint gp = new GradientPaint(0, 0, startColor, 0, height, endColor);
        g2d.setPaint(gp);
        g2d.fill(roundedRect);

        // Borde con efecto de luz
        GradientPaint borderGradient = new GradientPaint(
                0, 0, new Color(255, 255, 255, 50),
                0, height, new Color(0, 0, 0, 30)
        );
        g2d.setPaint(borderGradient);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(roundedRect);

        // Dibuja el icono si existe
        if (buttonIcon != null) {
            int iconWidth = buttonIcon.getIconWidth();
            int iconHeight = buttonIcon.getIconHeight();
            int x = (width - iconWidth) / 2;
            int y = (height - iconHeight) / 2;
            buttonIcon.paintIcon(this, g2d, x, y);
        }

        // Texto con sombra
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(getText())) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(getText(), x + 1, y + 1);

        g2d.setColor(foregroundColor);
        g2d.drawString(getText(), x, y);

        g2d.dispose();
    }

    private Color blendColors(Color c1, Color c2, float ratio) {
        int red = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
        int green = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
        int blue = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
        return new Color(red, green, blue);
    }

    // Métodos para personalizar la apariencia
    public void setArcRadius(int radius) {
        this.arcRadius = radius;
        repaint();
    }

    public void setReliefDepth(int depth) {
        this.reliefDepth = depth;
        repaint();
    }

    // Nuevo método para ajustar los colores
    public void setButtonColors(Color background, Color foreground) {
        this.backgroundColor = background;
        this.foregroundColor = foreground;
        repaint();
    }

    // Método para establecer el icono
    public void setButtonIcon(Icon icon) {
        this.buttonIcon = icon;
        repaint();
    }
}
