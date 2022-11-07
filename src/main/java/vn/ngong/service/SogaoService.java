package vn.ngong.service;

import vn.ngong.entity.Sogao;

import java.util.List;

public interface SogaoService {
    List<Sogao> list();

    Sogao getDetail(int id);
}
