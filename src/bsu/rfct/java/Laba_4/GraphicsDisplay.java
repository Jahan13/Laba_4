package bsu.rfct.java.Laba_4;
import java.awt.*;
import javax.swing.*;

public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    // Флаговые переменные, задающие правила отображения графика
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean showIntGraphics = false;
    // Границы диапазона пространства, подлежащего отображению
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    // Используемый масштаб отображения
    private double scale;
    // Различные стили черчения линий
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke graphicsIntStroke;
    // Различные шрифты отображения надписей
    private Font axisFont;

    public GraphicsDisplay() {
        // Цвет заднего фона области отображения - белый
        setBackground(Color.PINK);
        // Сконструировать необходимые объекты, используемые в рисовании
        // Перо для рисования графика
        graphicsStroke = new BasicStroke(5.0f, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_ROUND, 10.0f, new float[]
                {30, 10, 20, 10, 10, 10, 20, 10}, 0.0f);
        // Перо для рисования осей координат
        axisStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        // Перо для рисования контуров маркеров
        markerStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 45.0f, null, 0.0f);
        // Шрифт для подписей осей координат
        axisFont = new Font("Serif", Font.BOLD, 15);
    }
}