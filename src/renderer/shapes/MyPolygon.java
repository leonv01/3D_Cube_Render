package renderer.shapes;

import renderer.point.MyPoint;
import renderer.point.PointConverter;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MyPolygon {

    private MyPoint[] points;
    private Color color;

    public MyPolygon(Color color, MyPoint... points) {
        this.color = color;
        this.points = new MyPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            MyPoint p = points[i];
            this.points[i] = new MyPoint(p.x, p.y, p.z);
        }
    }

    public MyPolygon(MyPoint... points) {
        this.points = new MyPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            MyPoint p = points[i];
            this.points[i] = new MyPoint(p.x, p.y, p.z);
        }
    }

    public void render(Graphics g) {
        Polygon poly = new Polygon();
        for (MyPoint point : points) {
            Point p = PointConverter.convertPoint(point);
            poly.addPoint(p.x, p.y);
        }
        g.setColor(this.color);
        g.fillPolygon(poly);
    }

    public void rotate(boolean cw, double xDegrees, double yDegrees, double zDegrees) {
        for (MyPoint p : points) {
            PointConverter.rotateAxisX(p, cw, xDegrees);
            PointConverter.rotateAxisY(p, cw, yDegrees);
            PointConverter.rotateAxisX(p, cw, zDegrees);
        }
    }

    public double getAverageX(){
        double sum = 0;
        for (MyPoint p : this.points){
            sum += p.x;
        }
        return sum / points.length;
    }

    public static void sortPolygons(MyPolygon[] polygons) {
        List<MyPolygon> polygonsList = new ArrayList<MyPolygon>();

        for (MyPolygon poly : polygons) {
            polygonsList.add(poly);
        }

        Collections.sort(polygonsList, new Comparator<MyPolygon>() {
            @Override
            public int compare(MyPolygon p1, MyPolygon p2) {
                return p2.getAverageX() - p1.getAverageX() < 0 ? 1 : -1;
            }
        });

        for (int i = 0; i < polygons.length; i++){
            polygons[i] = polygonsList.get(i);
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
