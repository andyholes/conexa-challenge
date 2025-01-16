package ai.conexa.challenge.service.impl;

import ai.conexa.challenge.exception.ResourceNotFoundException;
import ai.conexa.challenge.model.mapper.BaseMapper;
import ai.conexa.challenge.model.mapper.VehicleMapper;
import ai.conexa.challenge.model.response.PaginatedResponse;
import ai.conexa.challenge.model.response.VehicleResponse;
import ai.conexa.challenge.service.VehicleService;
import ai.conexa.challenge.util.SwapiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static ai.conexa.challenge.util.MessageConstants.SWAPI_FETCHING_ERROR;
import static ai.conexa.challenge.util.MessageConstants.VEHICLE_NOT_FOUND;
import static ai.conexa.challenge.util.UrlConstants.NAME_PARAM;
import static ai.conexa.challenge.util.UrlConstants.PAGE_PARAM;
import static ai.conexa.challenge.util.UrlConstants.SIZE_PARAM;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    public static final String RESOURCE = "/vehicles";
    private final SwapiClient client;

    @Override
    public PaginatedResponse getAllPaginated(int page, int size) {
        String url = RESOURCE + PAGE_PARAM + page + SIZE_PARAM + size;
        ResponseEntity<String> response = client.fetchData(url);
        return BaseMapper.mapToPaginatedResponse(client.parseData(response));
    }

    @Override
    public VehicleResponse getById(Long id) {
        String url = RESOURCE + "/" + id;
        ResponseEntity<String> response;
        try{
            response = client.fetchData(url);
        } catch (HttpClientErrorException e){
            if (e.getRawStatusCode() == 404){
                throw new ResourceNotFoundException(VEHICLE_NOT_FOUND);
            }else {
                throw new RestClientException(SWAPI_FETCHING_ERROR);
            }
        }
        return VehicleMapper.mapToVehicleResponse(client.parseData(response));
    }

    @Override
    public List<VehicleResponse> getByName(String name) {
        String url = RESOURCE + NAME_PARAM + name;
        ResponseEntity<String> response = client.fetchData(url);
        return VehicleMapper.mapToVehicleResponseList(client.parseData(response));
    }
}
