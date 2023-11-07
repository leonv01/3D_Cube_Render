package renderer.shapes;

import java.awt.*;

public class Tetrahedron {

    private MyPolygon[] polygons;
    private Color color;

    public Tetrahedron(Color color, MyPolygon... polygons) {
        this.color = color;
        this.polygons = polygons;
        this.setPolygonColor();
    }

    public Tetrahedron(MyPolygon... polygons) {
        this.color = Color.WHITE;
        this.polygons = polygons;
    }

    public void render(Graphics g) {
        for (MyPolygon poly : polygons) {
            poly.render(g);
        }
    }

    public void rotate(boolean cw, double xDegrees, double yDegrees, double zDegrees) {
        for (MyPolygon p : polygons) {
            p.rotate(cw, xDegrees, yDegrees, zDegrees);
        }
        this.sortPolygons();
    }

    private void sortPolygons() {
        MyPolygon.sortPolygons(this.polygons);
    }

    private void setPolygonColor() {
        for (MyPolygon poly : polygons) {
            poly.setColor(this.color);
        }
    }
}
