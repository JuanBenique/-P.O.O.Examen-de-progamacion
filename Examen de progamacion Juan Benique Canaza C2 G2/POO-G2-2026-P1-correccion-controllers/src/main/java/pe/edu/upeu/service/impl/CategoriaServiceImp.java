package pe.edu.upeu.service.impl;

import pe.edu.upeu.model.Categoria;
import pe.edu.upeu.repository.CategoriaRepository;
import pe.edu.upeu.repository.ICrudGenericoRepository;
import pe.edu.upeu.service.ICategoriaService;

public class CategoriaServiceImp extends CrudGenericoServiceImp<Categoria, String> implements ICategoriaService {

    private final CategoriaRepository repo;
    public CategoriaServiceImp(CategoriaRepository repo) {
        this.repo = repo;
    }

    @Override
    protected ICrudGenericoRepository<Categoria, String> getRepo() {
        return repo;
    }
}
