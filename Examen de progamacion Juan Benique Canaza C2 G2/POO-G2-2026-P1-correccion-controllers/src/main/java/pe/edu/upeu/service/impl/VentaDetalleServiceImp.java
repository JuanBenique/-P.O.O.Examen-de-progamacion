package pe.edu.upeu.service.impl;

import pe.edu.upeu.model.VentaDetalle;
import pe.edu.upeu.repository.ICrudGenericoRepository;
import pe.edu.upeu.repository.VentaDetalleRepository;
import pe.edu.upeu.service.IVentaDetalleService;

public class VentaDetalleServiceImp extends CrudGenericoServiceImp<VentaDetalle, String> implements IVentaDetalleService {

    private final VentaDetalleRepository repo;
    public VentaDetalleServiceImp(VentaDetalleRepository repo) {
        this.repo = repo;
    }

    @Override
    protected ICrudGenericoRepository<VentaDetalle, String> getRepo() {
        return repo;
    }
}