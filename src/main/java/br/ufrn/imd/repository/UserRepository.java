package br.ufrn.imd.repository;

import br.ufrn.imd.model.User;

public interface UserRepository {
    User findById(Long id);
}
