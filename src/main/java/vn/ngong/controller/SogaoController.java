package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.entity.Sogao;
import vn.ngong.request.ProductFilterRequest;
import vn.ngong.response.ProductFilterResponse;
import vn.ngong.response.SogaoListResponse;
import vn.ngong.service.SogaoService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "sogaos")
public class SogaoController {

    @Autowired
    private SogaoService sogaoService;

    @Operation(summary = "API lấy danh sách sổ gạo", description = "")
    @RequestMapping(value = "", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SogaoListResponse> list() throws Exception {
        List<Sogao> sogaos =  sogaoService.list();
        SogaoListResponse res = SogaoListResponse
                .builder()
                .sogaos(sogaos)
                .code("00")
                .desc("Success")
                .build();

        return ResponseEntity.ok(res);
    }

//    @Operation(summary = "API lấy danh sách sổ gạo", description = "")
//    @RequestMapping(value = "", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<SogaoDetailResponse> getDetail() throws Exception {
//        List<Sogao> sogaos =  sogaoService.list();
//        SogaoListResponse res = SogaoListResponse
//                .builder()
//                .sogaos(sogaos)
//                .code("00")
//                .desc("Success")
//                .build();
//
//        return ResponseEntity.ok(res);
//    }
}

