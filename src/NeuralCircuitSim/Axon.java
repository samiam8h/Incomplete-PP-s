package NeuralCircuitSim;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

import java.util.LinkedHashSet;
import java.util.Set;

public class Axon extends Cylinder{
    private int[] Acoords, Bcoords, arrU, vectorW, midpoint, vectorV;
    private Point3D axisU;
    private Set<Byte> neurotransmitters = new LinkedHashSet();
    private Set<Byte> receptors = new LinkedHashSet();
    public float weight = 0; //Weight is a number between 0 and 1
    public float synapse = 0; // Synapse is a number, either -2 or 2
    private float affect = synapse * weight;
    double color0, color1, color2;

    public Axon(Neuron A, Neuron B, double radius, double color0, double color1, double color2){
        this.color0 = color0;
        this.color1 = color1;
        this.color2 = color2;
        super.setRadius(radius);
        PhongMaterial neuronMaterial = new PhongMaterial();
        neuronMaterial.setDiffuseColor(Color.color(this.color0,this.color1,this.color2));
        super.setMaterial(neuronMaterial);

        Acoords = new int[]{ A.getPositionX(), A.getPositionY(), A.getPositionZ()};
        Bcoords = new int[]{ B.getPositionX(), B.getPositionY(), B.getPositionZ()};

        vectorW = new int[]{
                Bcoords[0] - Acoords[0],
                Bcoords[1] - Acoords[1],
                Bcoords[2] - Acoords[2],
        };

        vectorV = new int[]{0, -1, 0};

        arrU = crossProduct(vectorV, vectorW);

        axisU = new Point3D(arrU[0], arrU[1], arrU[2]);

        midpoint = new int[]{
                (Acoords[0] + Bcoords[0]) / 2,
                (Acoords[1] + Bcoords[1]) / 2,
                (Acoords[2] + Bcoords[2]) / 2
        };

        super.setHeight(magnitude(vectorW));
        super.getTransforms().add(new Rotate(Math.toDegrees( Math.acos(dotProduct(vectorV, vectorW) / ((magnitude(vectorV) + 0.0) * (magnitude(vectorW) + 0.0)) )) , axisU));
        super.translateXProperty().set(midpoint[0]);
        super.translateYProperty().set(midpoint[1]);
        super.translateZProperty().set(midpoint[2]);
    }

    public void setSynapse(float synapse){ this.synapse = synapse; }

    public float getSynapse(){ return synapse; }

    public void setWeight(float weight){ this.weight = weight; }

    public float getWeight(){ return weight; }

    public Point3D getAxisU() { return axisU; }

    public Cylinder prepareAxon(){
        Cylinder cyl = new Cylinder(5, magnitude(vectorW), 8);

        cyl.getTransforms().add(new Rotate(Math.toDegrees( Math.acos(dotProduct(vectorV, vectorW) / ((magnitude(vectorV) + 0.0) * (magnitude(vectorW) + 0.0)) )) , axisU));
        cyl.translateXProperty().set(midpoint[0]);
        cyl.translateYProperty().set(midpoint[1]);
        cyl.translateZProperty().set(midpoint[2]);
        return cyl;
    }

    private int[] crossProduct(int[] v, int[] w){
        if(v.length != w.length) return null;
        int[] res = new int[v.length];

        res[0] = v[1] * w[2] - v[2] * w[1];
        res[1] = v[0] * w[2] - v[2] * w[0];
        res[2] = v[0] * w[1] - v[1] * w[0];

        return res;
    }

    private int dotProduct(int[] v, int[] w){ return v[0] * w[0] + v[1] * w[1] + v[2] * w[2]; }

    private int magnitude(int[] v){ return (int) Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]); }

}
