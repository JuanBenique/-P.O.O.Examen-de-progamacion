package pe.edu.upeu.service.impl;

import pe.edu.upeu.model.Cliente;
import pe.edu.upeu.repository.ClienteRepository;
import pe.edu.upeu.repository.ICrudGenericoRepository;
import pe.edu.upeu.service.IClienteService;

public class ClienteServiceImp extends CrudGenericoServiceImp<Cliente, String> implements IClienteService {

    private final ClienteRepository repo;
    public ClienteServiceImp(ClienteRepository repo) {
        this.repo = repo;
    }

    @Override
    protected ICrudGenericoRepository<Cliente, String> getRepo() {
        return repo;
    }
}
