package in.gov.egramswaraj.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.entity.ApplicationEntity;
import in.gov.egramswaraj.request.ApplicationRequest;
import in.gov.egramswaraj.response.ApplicationResponse;
import in.gov.egramswaraj.service.ApplicationService;

@ExtendWith(MockitoExtension.class)
class ApplicationRestControllerTest {

	@InjectMocks
	private ApplicationRestController controller;

	@Mock
	private ApplicationService applicationService;

	@Mock
	private ModelMapper modelMapper;

	private ApplicationEntity entity;
	private ApplicationRequest request;
	private ApplicationResponse responseDto;

	@BeforeEach
	void setup() {
		request = new ApplicationRequest();
		request.setApplicationName("MyApp");
		request.setApplicationUrl("https://myapp.com");

		entity = new ApplicationEntity();
		entity.setId(1L);
		entity.setApplicationName("MyApp");
		entity.setApplicationUrl("https://myapp.com");

		responseDto = new ApplicationResponse();
		responseDto.setId(1L);
		responseDto.setApplicationName("MyApp");
		responseDto.setApplicationUrl("https://myapp.com");
	}

	@Test
    void shouldCreateApplication() {
        when(applicationService.createApplication(request)).thenReturn(entity);
        when(modelMapper.map(entity, ApplicationResponse.class)).thenReturn(responseDto);

        ApplicationResponse response = controller.createApplication(request);

        assertThat(response).isNotNull();
        assertThat(response.getApplicationName()).isEqualTo("MyApp");

        verify(applicationService).createApplication(request);
    }

	@Test
    void shouldUpdateApplicationStatus() {
        when(applicationService.updateApplicationStatus(1L, true)).thenReturn(AppConstant.APPLICATION_STATUS);

        ResponseEntity<String> response = controller.updateApplicationStatus(1L, true);

        assertThat(response.getBody()).isEqualTo(AppConstant.APPLICATION_STATUS);
        verify(applicationService).updateApplicationStatus(1L, true);
    }

	@Test
    void shouldDeleteApplication() {
        when(applicationService.deleteApplication(1L)).thenReturn(AppConstant.APPLICATION_DELETION);

        ResponseEntity<String> response = controller.deleteApplication(1L);

        assertThat(response.getBody()).isEqualTo(AppConstant.APPLICATION_DELETION);
        verify(applicationService).deleteApplication(1L);
    }

	@Test
	void shouldGetAllApplications() {
		Page<ApplicationEntity> page = new PageImpl<>(Collections.singletonList(entity));

		when(applicationService.getAllApplications(0, 100)).thenReturn(page);
		when(modelMapper.map(entity, ApplicationResponse.class)).thenReturn(responseDto);

		ResponseEntity<Page<ApplicationResponse>> response = controller.getAllApplications(0, 100);

		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent()).hasSize(1);
		assertThat(response.getBody().getContent().get(0).getApplicationName()).isEqualTo("MyApp");

		verify(applicationService).getAllApplications(0, 100);
	}

	@Test
    void shouldGetApplicationById() {
        when(applicationService.getApplicationById(1L)).thenReturn(entity);
        when(modelMapper.map(entity, ApplicationResponse.class)).thenReturn(responseDto);

        ResponseEntity<ApplicationResponse> response = controller.getApplicationById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getApplicationName()).isEqualTo("MyApp");

        verify(applicationService).getApplicationById(1L);
    }

	@Test
    void shouldUpdateApplication() {
        when(applicationService.updateApplication(1L, request)).thenReturn(entity);
        when(modelMapper.map(entity, ApplicationResponse.class)).thenReturn(responseDto);

        ResponseEntity<ApplicationResponse> response = controller.updateApplication(1L, request);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getApplicationUrl()).isEqualTo("https://myapp.com");

        verify(applicationService).updateApplication(1L, request);
    }
}
