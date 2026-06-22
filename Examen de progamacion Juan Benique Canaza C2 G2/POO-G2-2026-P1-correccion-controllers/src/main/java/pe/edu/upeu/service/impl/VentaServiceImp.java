package pe.edu.upeu.service.impl;

import pe.edu.upeu.model.Venta;
import pe.edu.upeu.repository.ICrudGenericoRepository;
import pe.edu.upeu.repository.VentaRepository;
import pe.edu.upeu.service.IVentaService;

public class VentaServiceImp extends CrudGenericoServiceImp<Venta, String> implements IVentaService {

    private final VentaRepository repo;
    public VentaServiceImp(VentaRepository repo) {
        this.repo = repo;
    }

    @Override
    protected ICrudGenericoRepository<Venta, String> getRepo() {
        return repo;
    }
}
