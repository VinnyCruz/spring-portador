package org.jazztech.portador.apicredit;

import java.util.UUID;
import org.jazztech.portador.apicredit.dto.CreditAnalysis;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CreditAnalysis", url = "http://localhost:9001/v1.0/creditanalysis")
public interface CreditApi {
    @GetMapping(path = "/{id}")
    CreditAnalysis getAnalysisById(@PathVariable(value = "id") UUID analysisId);
}
