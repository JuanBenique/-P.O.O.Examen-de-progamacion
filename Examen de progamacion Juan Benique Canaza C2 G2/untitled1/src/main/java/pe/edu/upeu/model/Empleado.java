package pe.edu.upeu.model;

import javafx.beans.property.SimpleStringProperty;

public class Empleado {

    private SimpleStringProperty nombre;
    private SimpleStringProperty numero;
    private SimpleStringProperty area;
    private SimpleStringProperty cargo;
    private SimpleStringProperty fecha;

    public Empleado(String nombre, String numero, String area, String cargo, String fecha) {
        this.nombre = new SimpleStringProperty(nombre);
        this.numero = new SimpleStringProperty(numero);
        this.area = new SimpleStringProperty(area);
        this.cargo = new SimpleStringProperty(cargo);
        this.fecha = new SimpleStringProperty(fecha);
    }


    public String getNombre() { return nombre.get(); }
    public String getNumero() { return numero.get(); }
    public String getArea() { return area.get(); }
    public String getCargo() { return cargo.get(); }
    public String getFecha() { return fecha.get(); }

//cambios de nombre (editar)
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public void setNumero(String numero) { this.numero.set(numero); }
    public void setArea(String area) { this.area.set(area); }
    public void setCargo(String cargo) { this.cargo.set(cargo); }
    public void setFecha(String fecha) { this.fecha.set(fecha); }
}