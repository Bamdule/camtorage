package com.camtorage.db.serverconfig.service;

import com.camtorage.db.serverconfig.repository.ServerConfigRepository;
import com.camtorage.entity.serverconfig.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServerConfigServiceImpl implements ServerConfigService {

    @Autowired
    private ServerConfigRepository serverConfigRepository;

    @Override
    public Map<String, String> getServiceConfigMap() {

        Map<String, String> serverConfig = new LinkedHashMap<>();
        List<ServerConfig> serverConfigs = serverConfigRepository.findAll();

        serverConfigs.forEach(sc -> {
            serverConfig.put(sc.getName(), sc.getValue());
        });


        return serverConfig;
    }
}
