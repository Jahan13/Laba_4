package bsu.rfct.java.Laba_4;
import java.awt.*;
import javax.swing.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    // Флаговые переменные, задающие правила отображения графика
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean showIntGraphics = false;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private double scale;

    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke graphicsIntStroke;

    private Font axisFont;

    public GraphicsDisplay() {

        setBackground(Color.PINK);

        graphicsStroke = new BasicStroke(5.0f, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_ROUND, 10.0f, new float[]
                {30, 10, 20, 10, 10, 10, 20, 10}, 0.0f);

        axisStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);

        markerStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 45.0f, null, 0.0f);

        axisFont = new Font("Serif", Font.BOLD, 15);
    }


    public void showGraphics(Double[][] graphicsData) {

        this.graphicsData = graphicsData;

        repaint();
    }


    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0) return;

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

        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);

        scale = Math.min(scaleX, scaleY);

        if (scale == scaleX) {

            double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {

            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }


    Graphics2D canvas = (Graphics2D) g;
    Stroke oldStroke = canvas.getStroke();
    Color oldColor = canvas.getColor();
    Paint oldPaint = canvas.getPaint();
    Font oldFont = canvas.getFont();

        if(showAxis)

    paintAxis(canvas);


    paintGraphics(canvas);

        if(showMarkers)

    paintMarkers(canvas);

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
}


    protected void paintGraphics(Graphics2D canvas) {

        canvas.setStroke(graphicsStroke);

        canvas.setColor(Color.RED);

        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {

            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {

                graphics.lineTo(point.getX(), point.getY());
            } else {

                graphics.moveTo(point.getX(), point.getY());
            }
        }

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


    protected void paintMarkers(Graphics2D canvas) {

        canvas.setStroke(markerStroke);

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

    protected void paintAxis(Graphics2D canvas) {

        canvas.setStroke(axisStroke);

        canvas.setColor(Color.BLACK);

        canvas.setPaint(Color.BLACK);

        canvas.setFont(axisFont);

        FontRenderContext context = canvas.getFontRenderContext();
        Point2D.Double labelPos1 = xyToPoint( 0, 0);

        canvas.drawString("0", (float) labelPos1.getX()+10 ,
                (float) (labelPos1.getY() - 10));

        if (minX <= 0.0 && maxX >= 0.0) {

            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));

            GeneralPath arrow = new GeneralPath();

            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());

            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);

            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());

            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);

            canvas.drawString("y", (float) labelPos.getX() + 10,
                    (float) (labelPos.getY() - bounds.getY()));
        }

        if (minY <= 0.0 && maxY >= 0.0) {

            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));

            GeneralPath arrow = new GeneralPath();

            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());

            arrow.lineTo(arrow.getCurrentPoint().getX() - 20,
                    arrow.getCurrentPoint().getY() - 5);

            arrow.lineTo(arrow.getCurrentPoint().getX(),
                    arrow.getCurrentPoint().getY() + 10);

            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
            canvas.drawString("x", (float)(labelPos.getX() -
                    bounds.getWidth() - 10), (float)(labelPos.getY() + bounds.getY()));
        }
    }

    protected Point2D.Double xyToPoint(double x, double y)
    {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX*scale, deltaY*scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY)
    {

        Point2D.Double dest = new Point2D.Double();

        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
}
