package pe.edu.upeu.service;

import pe.edu.upeu.model.Empleado;
import pe.edu.upeu.repository.EmpleadoRepository;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoServiceImp {

    private EmpleadoRepository repo = new EmpleadoRepository();

    //  Validar número único
    public boolean existeNumero(String numero) {
        for (Empleado e : repo.findAll()) {
            if (e.getNumero().equals(numero)) {
                return true;
            }
        }
        return false;
    }

    // Guardar
    public void guardar(Empleado e) {
        if (!existeNumero(e.getNumero())) {
            repo.save(e);
        } else {
            System.out.println("Número duplicado");
        }
    }

    // 🔹 Buscar por área
    public List<Empleado> buscarArea(String area) {
        List<Empleado> lista = new ArrayList<>();
        for (Empleado e : repo.findAll()) {
            if (e.getArea().equalsIgnoreCase(area)) {
                lista.add(e);
            }
        }
        return lista;
    }

    //buscran por el  cargo
    public List<Empleado> buscarCargo(String cargo) {
        List<Empleado> lista = new ArrayList<>();
        for (Empleado e : repo.findAll()) {
            if (e.getCargo().equalsIgnoreCase(cargo)) {
                lista.add(e);
            }
        }
        return lista;
    }

    // 🔹 Obtener todos
    public List<Empleado> getAll() {
        return repo.findAll();
    }


    public void eliminar(Empleado e) {
        repo.findAll().remove(e);
    }

    //edita
    public void editar(int index, Empleado nuevo) {
        repo.findAll().set(index, nuevo);
    }
}