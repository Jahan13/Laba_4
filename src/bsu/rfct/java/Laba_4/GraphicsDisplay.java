package bsu.rfct.java.Laba_4;
import java.awt.*;
import javax.swing.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;


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

    // Данный метод вызывается из обработчика элемента меню "Открыть файл с графиком"
    // главного окна приложения в случае успешной загрузки данных
    public void showGraphics(Double[][] graphicsData) {
        // Сохранить массив точек во внутреннем поле класса
        this.graphicsData = graphicsData;
        // Запросить перерисовку компонента, т.е. неявно вызвать paintComponent()
        repaint();
    }

    // Методы-модификаторы для изменения параметров отображения графика
    // Изменение любого параметра приводит к перерисовке области
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void paintComponent(Graphics g) {
        /* Шаг 1 - Вызвать метод предка для заливки области цветом заднего фона
         * Эта функциональность - единственное, что осталось в наследство от
         * paintComponent класса JPanel
         */
        super.paintComponent(g);
        // Шаг 2 - Если данные графика не загружены (при показе компонента при запуске программы) - ничего не делать
        if (graphicsData == null || graphicsData.length == 0) return;
        // Шаг 3 - Определить минимальное и максимальное значения для координат X и Y
        // Это необходимо для определения области пространства, подлежащей отображению
        // Е? верхний левый угол это (minX, maxY) - правый нижний это (maxX, minY)
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;
        // Найти минимальное и максимальное значение функции
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }
        /* Шаг 4 - Определить (исходя из размеров окна) масштабы по осям X и Y - сколько пикселов
         * приходится на единицу длины по X и по Y
         */
        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        // Шаг 5 - Чтобы изображение было неискаж?нным - масштаб должен быть одинаков
        // Выбираем за основу минимальный
        scale = Math.min(scaleX, scaleY);
        // Шаг 6 - корректировка границ отображаемой области согласно выбранному масштабу
        if (scale == scaleX) {
            /* Если за основу был взят масштаб по оси X, значит по оси Y делений меньше,
             * т.е. подлежащий визуализации диапазон по Y будет меньше высоты окна.
             * Значит необходимо добавить делений, сделаем это так:
             * 1) Вычислим, сколько делений влезет по Y при выбранном масштабе - getSize().getHeight()/scale
             * 2) Вычтем из этого сколько делений требовалось изначально
             * 3) Набросим по половине недостающего расстояния на maxY и minY
             */
            double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {
            // Если за основу был взят масштаб по оси Y, действовать по аналогии
            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }

    // Шаг 7 - Сохранить текущие настройки холста
    Graphics2D canvas = (Graphics2D) g;
    Stroke oldStroke = canvas.getStroke();
    Color oldColor = canvas.getColor();
    Paint oldPaint = canvas.getPaint();
    Font oldFont = canvas.getFont();
    // Шаг 8 - В нужном порядке вызвать методы отображения элементов графика
    // Порядок вызова методов имеет значение, т.к. предыдущий рисунок будет затираться последующим
    // Первыми (если нужно) отрисовываются оси координат.
        if(showAxis)

    paintAxis(canvas);

    // Затем отображается сам график
    paintGraphics(canvas);
    // Затем (если нужно) отображаются маркеры точек, по которым строился график.
        if(showMarkers)

    paintMarkers(canvas);
    // Шаг 9 - Восстановить старые настройки холста
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
}

    // Отрисовка графика по прочитанным координатам
    protected void paintGraphics(Graphics2D canvas) {
        // Выбрать линию для рисования графика
        canvas.setStroke(graphicsStroke);
        // Выбрать цвет линии
        canvas.setColor(Color.RED);
        /* Будем рисовать линию графика как путь, состоящий из множества сегментов (GeneralPath)
         * Начало пути устанавливается в первую точку графика, после чего прямой соединяется со
         * следующими точками
         */
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {
            // Преобразовать значения (x,y) в точку на экране point
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
                // Не первая итерация цикла - вести линию в точку point
                graphics.lineTo(point.getX(), point.getY());
            } else {
                // Первая итерация цикла - установить начало пути в точку point
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        // Отобразить график
        canvas.draw(graphics);
    }
    private boolean markPoint(double y)
    {
        int n = (int) y;
        if (n < 0)
            n *= (-1);
        while (n != 0)
        {
            int q = n - (n / 10) * 10;
            if (q % 2 != 0)
                return false;
            n = n / 10;
        }
        return true;
    }

    // Отображение маркеров точек, по которым рисовался график
    protected void paintMarkers(Graphics2D canvas) {
        // Шаг 1 - Установить специальное перо для черчения контуров маркеров
        canvas.setStroke(markerStroke);
        // Выбрать красный цвета для контуров маркеров
        canvas.setColor(Color.BLACK);
        for(int i= 0;i<graphicsData.length;i++){
            Boolean flag=true;
            if(i!=0 && i!=graphicsData.length-1 &&((graphicsData[i-1][1]<graphicsData[i][1] && graphicsData[i][1]>graphicsData[i+1][1]) || (graphicsData[i-1][1]>graphicsData[i][1] && graphicsData[i][1]<graphicsData[i+1][1])))
            {
                canvas.setColor(Color.RED);
                flag=false;
            }
            else if(markPoint(graphicsData[i][1]))
                canvas.setColor(Color.BLUE);
            else
                canvas.setColor(Color.BLACK);

            GeneralPath path = new GeneralPath();
            Point2D.Double center = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            path.moveTo(center.x, center.y + 5);
            path.lineTo(center.x + 5, center.y);
            path.lineTo(center.x, center.y - 5);
            path.lineTo(center.x - 5, center.y);
            path.lineTo(center.x, center.y + 5);
            canvas.draw(path);
            if (flag == false)
            {
                FontRenderContext context = canvas.getFontRenderContext();
                Rectangle2D bounds = axisFont.getStringBounds("Экстремум", context);
                Point2D.Double labelPos = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
                canvas.drawString("Экстремум", (float) labelPos.getX() + 5, (float) (labelPos.getY() - bounds.getY()));
                canvas.drawString("("+graphicsData[i][0]+"; "+graphicsData[i][1]+")", (float) labelPos.getX() + 5, (float) (labelPos.getY() - bounds.getY()) - 20);
            }
        }
    }

}