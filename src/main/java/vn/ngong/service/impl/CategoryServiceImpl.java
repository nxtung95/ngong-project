package vn.ngong.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.entity.Category;
import vn.ngong.repository.CategoryRepository;
import vn.ngong.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> list() {
        return categoryRepository.findAllByStatus(1);
    }
}
