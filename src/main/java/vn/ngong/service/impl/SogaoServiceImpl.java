package vn.ngong.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.entity.Sogao;
import vn.ngong.repository.SoGaoRepository;
import vn.ngong.service.SogaoService;

import java.util.List;

@Service
public class SogaoServiceImpl implements SogaoService {

    @Autowired
    private SoGaoRepository soGaoRepository;

    @Override
    public List<Sogao> list() {
        return soGaoRepository.findAllByStatusByOrderByOrderNumber(1);
    }

    @Override
    public Sogao getDetail(int id) {
        return soGaoRepository.findById(id);
    }
}
