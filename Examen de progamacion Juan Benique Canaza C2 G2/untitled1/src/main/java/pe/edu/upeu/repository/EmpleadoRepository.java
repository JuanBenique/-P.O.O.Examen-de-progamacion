package pe.edu.upeu.repository;

import pe.edu.upeu.model.Empleado;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepository {

    private List<Empleado> lista = new ArrayList<>();

    // 🔹 Guardar
    public void save(Empleado e) {
        lista.add(e);
    }


    public List<Empleado> findAll() {
        return lista;
    }

    // deleteteliminar
    public void delete(Empleado e) {
        lista.remove(e);
    }

    // modificar las cosas onombres
    public void update(int index, Empleado e) {
        lista.set(index, e);
    }
}