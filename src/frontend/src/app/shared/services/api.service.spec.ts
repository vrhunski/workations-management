import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  ApiService,
  WorkationRequest,
  WorkationResponse,
  PageResponse,
  WorkationQueryParams
} from './api.service';

describe('ApiService Integration Tests', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/v1/workations';

  // Mock data
  const mockWorkation: WorkationResponse = {
    id: 1,
    employee: 'John Doe',
    country: 'United States',
    countryDest: 'Portugal',
    startDate: '2025-03-15',
    endDate: '2025-05-29',
    days: 75,
    risk: 'LOW_RISK'
  };

  const mockPageResponse: PageResponse<WorkationResponse> = {
    content: [mockWorkation],
    pageable: {
      pageNumber: 0,
      pageSize: 10,
      sort: {
        sorted: true,
        unsorted: false,
        empty: false
      },
      offset: 0,
      paged: true,
      unpaged: false
    },
    totalPages: 5,
    totalElements: 50,
    last: false,
    size: 10,
    number: 0,
    sort: {
      sorted: true,
      unsorted: false,
      empty: false
    },
    numberOfElements: 10,
    first: true,
    empty: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure no outstanding HTTP requests
  });

  describe('getAllWorkations', () => {
    it('should fetch all workations without parameters', () => {
      service.getAllWorkations().subscribe(response => {
        expect(response).toEqual(mockPageResponse);
        expect(response.content.length).toBe(1);
        expect(response.totalElements).toBe(50);
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('GET');
      expect(req.request.params.keys().length).toBe(0);
      req.flush(mockPageResponse);
    });

    it('should fetch workations with pagination parameters', () => {
      const params: WorkationQueryParams = {
        page: 1,
        size: 20
      };

      service.getAllWorkations(params).subscribe(response => {
        expect(response).toEqual(mockPageResponse);
      });

      const req = httpMock.expectOne(request =>
        request.url === apiUrl &&
        request.params.get('page') === '1' &&
        request.params.get('size') === '20'
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockPageResponse);
    });

    it('should fetch workations with sorting parameters', () => {
      const params: WorkationQueryParams = {
        sortBy: 'employee',
        sortDirection: 'DESC'
      };

      service.getAllWorkations(params).subscribe(response => {
        expect(response).toEqual(mockPageResponse);
      });

      const req = httpMock.expectOne(request =>
        request.url === apiUrl &&
        request.params.get('sortBy') === 'employee' &&
        request.params.get('sortDirection') === 'DESC'
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockPageResponse);
    });

    it('should fetch workations with filter parameters', () => {
      const params: WorkationQueryParams = {
        employee: 'John',
        country: 'United States',
        countryDest: 'Portugal',
        risk: 'LOW_RISK'
      };

      service.getAllWorkations(params).subscribe(response => {
        expect(response).toEqual(mockPageResponse);
      });

      const req = httpMock.expectOne(request =>
        request.url === apiUrl &&
        request.params.get('employee') === 'John' &&
        request.params.get('country') === 'United States' &&
        request.params.get('countryDest') === 'Portugal' &&
        request.params.get('risk') === 'LOW_RISK'
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockPageResponse);
    });

    it('should fetch workations with all parameters combined', () => {
      const params: WorkationQueryParams = {
        page: 2,
        size: 15,
        sortBy: 'days',
        sortDirection: 'ASC',
        employee: 'Sarah',
        country: 'Canada',
        countryDest: 'Iceland',
        risk: 'NO_RISK'
      };

      service.getAllWorkations(params).subscribe(response => {
        expect(response).toEqual(mockPageResponse);
      });

      const req = httpMock.expectOne(request => {
        const params = request.params;
        return request.url === apiUrl &&
          params.get('page') === '2' &&
          params.get('size') === '15' &&
          params.get('sortBy') === 'days' &&
          params.get('sortDirection') === 'ASC' &&
          params.get('employee') === 'Sarah' &&
          params.get('country') === 'Canada' &&
          params.get('countryDest') === 'Iceland' &&
          params.get('risk') === 'NO_RISK';
      });
      expect(req.request.method).toBe('GET');
      req.flush(mockPageResponse);
    });

    it('should handle empty response', () => {
      const emptyResponse: PageResponse<WorkationResponse> = {
        ...mockPageResponse,
        content: [],
        totalElements: 0,
        totalPages: 0,
        numberOfElements: 0,
        empty: true
      };

      service.getAllWorkations().subscribe(response => {
        expect(response.content.length).toBe(0);
        expect(response.empty).toBe(true);
      });

      const req = httpMock.expectOne(apiUrl);
      req.flush(emptyResponse);
    });

    it('should handle error response', () => {
      const errorMessage = 'Failed to load workations';

      service.getAllWorkations().subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(500);
          expect(error.statusText).toBe('Internal Server Error');
        }
      });

      const req = httpMock.expectOne(apiUrl);
      req.flush(errorMessage, { status: 500, statusText: 'Internal Server Error' });
    });
  });

  describe('getWorkationById', () => {
    it('should fetch a single workation by id', () => {
      const workationId = 1;

      service.getWorkationById(workationId).subscribe(response => {
        expect(response).toEqual(mockWorkation);
        expect(response.id).toBe(workationId);
      });

      const req = httpMock.expectOne(`${apiUrl}/${workationId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockWorkation);
    });

    it('should handle 404 when workation not found', () => {
      const workationId = 999;

      service.getWorkationById(workationId).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(404);
          expect(error.statusText).toBe('Not Found');
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/${workationId}`);
      req.flush('Workation not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('createWorkation', () => {
    it('should create a new workation', () => {
      const newWorkation: WorkationRequest = {
        employee: 'Jane Smith',
        country: 'Germany',
        countryDest: 'Spain',
        startDate: '2025-06-01',
        endDate: '2025-08-15',
        days: 75,
        risk: 'LOW_RISK'
      };

      const createdWorkation: WorkationResponse = {
        id: 2,
        ...newWorkation
      };

      service.createWorkation(newWorkation).subscribe(response => {
        expect(response).toEqual(createdWorkation);
        expect(response.id).toBe(2);
        expect(response.employee).toBe('Jane Smith');
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newWorkation);
      req.flush(createdWorkation);
    });

    it('should handle validation errors when creating workation', () => {
      const invalidWorkation: WorkationRequest = {
        employee: '',
        country: 'Germany',
        countryDest: 'Spain',
        startDate: '2025-06-01',
        endDate: '2025-08-15',
        days: 75,
        risk: 'LOW_RISK'
      };

      service.createWorkation(invalidWorkation).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(400);
          expect(error.statusText).toBe('Bad Request');
        }
      });

      const req = httpMock.expectOne(apiUrl);
      req.flush('Validation error', { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('updateWorkation', () => {
    it('should update an existing workation', () => {
      const workationId = 1;
      const updatedData: WorkationRequest = {
        employee: 'John Doe Updated',
        country: 'United States',
        countryDest: 'Spain',
        startDate: '2025-03-15',
        endDate: '2025-06-30',
        days: 107,
        risk: 'HIGH_RISK'
      };

      const updatedWorkation: WorkationResponse = {
        id: workationId,
        ...updatedData
      };

      service.updateWorkation(workationId, updatedData).subscribe(response => {
        expect(response).toEqual(updatedWorkation);
        expect(response.employee).toBe('John Doe Updated');
        expect(response.days).toBe(107);
        expect(response.risk).toBe('HIGH_RISK');
      });

      const req = httpMock.expectOne(`${apiUrl}/${workationId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatedData);
      req.flush(updatedWorkation);
    });

    it('should handle 404 when updating non-existent workation', () => {
      const workationId = 999;
      const updatedData: WorkationRequest = {
        employee: 'Non Existent',
        country: 'USA',
        countryDest: 'Canada',
        startDate: '2025-01-01',
        endDate: '2025-02-01',
        days: 31,
        risk: 'NO_RISK'
      };

      service.updateWorkation(workationId, updatedData).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/${workationId}`);
      req.flush('Workation not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('deleteWorkation', () => {
    it('should delete a workation', () => {
      const workationId = 1;

      service.deleteWorkation(workationId).subscribe(response => {
        expect(response).toBeUndefined();
      });

      const req = httpMock.expectOne(`${apiUrl}/${workationId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });

    it('should handle 404 when deleting non-existent workation', () => {
      const workationId = 999;

      service.deleteWorkation(workationId).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/${workationId}`);
      req.flush('Workation not found', { status: 404, statusText: 'Not Found' });
    });

    it('should handle 500 error when deleting workation', () => {
      const workationId = 1;

      service.deleteWorkation(workationId).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(500);
          expect(error.statusText).toBe('Internal Server Error');
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/${workationId}`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });

  describe('HTTP Headers and Configuration', () => {
    it('should send requests with correct content type', () => {
      const newWorkation: WorkationRequest = {
        employee: 'Test User',
        country: 'France',
        countryDest: 'Italy',
        startDate: '2025-07-01',
        endDate: '2025-07-31',
        days: 30,
        risk: 'NO_RISK'
      };

      service.createWorkation(newWorkation).subscribe();

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.headers.has('Content-Type')).toBeFalsy(); // Angular sets this automatically
      req.flush({ id: 3, ...newWorkation });
    });
  });

  describe('Edge Cases', () => {
    it('should handle workation with maximum days (365)', () => {
      const longWorkation: WorkationRequest = {
        employee: 'Long Term Worker',
        country: 'Australia',
        countryDest: 'New Zealand',
        startDate: '2025-01-01',
        endDate: '2025-12-31',
        days: 365,
        risk: 'HIGH_RISK'
      };

      service.createWorkation(longWorkation).subscribe(response => {
        expect(response.days).toBe(365);
      });

      const req = httpMock.expectOne(apiUrl);
      req.flush({ id: 10, ...longWorkation });
    });

    it('should handle workation with minimum days (1)', () => {
      const shortWorkation: WorkationRequest = {
        employee: 'Short Term Worker',
        country: 'Belgium',
        countryDest: 'Luxembourg',
        startDate: '2025-05-01',
        endDate: '2025-05-02',
        days: 1,
        risk: 'NO_RISK'
      };

      service.createWorkation(shortWorkation).subscribe(response => {
        expect(response.days).toBe(1);
      });

      const req = httpMock.expectOne(apiUrl);
      req.flush({ id: 11, ...shortWorkation });
    });

    it('should handle special characters in filter parameters', () => {
      const params: WorkationQueryParams = {
        employee: "O'Brien",
        country: 'Côte d\'Ivoire'
      };

      service.getAllWorkations(params).subscribe();

      const req = httpMock.expectOne(request =>
        request.url === apiUrl &&
        request.params.has('employee') &&
        request.params.has('country')
      );
      req.flush(mockPageResponse);
    });
  });
});
