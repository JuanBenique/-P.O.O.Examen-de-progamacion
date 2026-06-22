package pe.edu.upeu.service.impl;

import pe.edu.upeu.model.Producto;
import pe.edu.upeu.repository.ICrudGenericoRepository;
import pe.edu.upeu.repository.ProductoRepository;
import pe.edu.upeu.service.IProductoService;

public class ProductoServiceImp extends CrudGenericoServiceImp<Producto, String> implements IProductoService {

    private final ProductoRepository repo;
    public ProductoServiceImp(ProductoRepository repo) {
        this.repo = repo;
    }

    @Override
    protected ICrudGenericoRepository<Producto, String> getRepo() {
        return repo;
    }
}
