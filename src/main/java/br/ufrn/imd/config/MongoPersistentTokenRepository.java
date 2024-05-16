package br.ufrn.imd.config;

import br.ufrn.imd.model.PersistentLogin;
import br.ufrn.imd.repository.PersistentLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;
import java.util.Optional;

public class MongoPersistentTokenRepository implements PersistentTokenRepository {

    @Autowired
    private PersistentLoginRepository persistentLoginRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogin login = new PersistentLogin();
        login.setSeries(token.getSeries());
        login.setUsername(token.getUsername());
        login.setToken(token.getTokenValue());
        login.setLastUsed(token.getDate());
        persistentLoginRepository.save(login);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        Optional<PersistentLogin> loginOptional = persistentLoginRepository.findBySeries(series);
        if (loginOptional.isPresent()) {
            PersistentLogin login = loginOptional.get();
            login.setToken(tokenValue);
            login.setLastUsed(lastUsed);
            persistentLoginRepository.save(login);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Optional<PersistentLogin> loginOptional = persistentLoginRepository.findBySeries(seriesId);
        if (loginOptional.isPresent()) {
            PersistentLogin login = loginOptional.get();
            return new PersistentRememberMeToken(login.getUsername(), login.getSeries(), login.getToken(), login.getLastUsed());
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        persistentLoginRepository.deleteByUsername(username);
    }
}
