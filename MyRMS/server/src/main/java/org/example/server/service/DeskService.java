package org.example.server.service;

import org.example.pojo.entity.Desk;
import java.util.List;

public interface DeskService {
    List<Desk> fetchAllTables();
}