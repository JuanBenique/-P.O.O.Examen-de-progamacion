package pe.edu.upeu.service.impl;

import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.repository.ICrudGenericoRepository;
import pe.edu.upeu.repository.UsuarioRepository;
import pe.edu.upeu.service.IUsuarioService;

import java.util.Optional;

public class UsuarioServiceImp extends CrudGenericoServiceImp<Usuario, String> implements IUsuarioService {

    private final UsuarioRepository repo;

    public UsuarioServiceImp(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    protected ICrudGenericoRepository<Usuario, String> getRepo() {
        return (ICrudGenericoRepository<Usuario, String>) repo;
    }

    @Override
    public Optional<Usuario> loginUsuario(String usuario, String clave) {
        return repo.findByUsuarioAndClave(usuario, clave);
    }
}