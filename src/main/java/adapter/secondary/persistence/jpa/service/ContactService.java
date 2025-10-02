package adapter.secondary.persistence.jpa.service;

import java.util.List;
import java.util.Optional;

import domain.contact.Contact;
import domain.contact.ContactRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import adapter.secondary.persistence.jpa.entity.ContactEntity;
import adapter.secondary.persistence.jpa.mapper.ContactMapper;
import adapter.secondary.persistence.jpa.repository.JpaContactRepository;
import adapter.primary.http.security.TenantContext;

@Service
@Profile("prod")
public class ContactService implements ContactRepository {
	
	private final JpaContactRepository repository;
	private final ContactMapper mapper;
	
	public ContactService(JpaContactRepository repository, ContactMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}
	
	@Override
	public Optional<Contact> find(String nickName) {
		return repository.findByNickName(nickName)
				.map(mapper::toModel);
	}
	
	@Override
	public List<Contact> findAll() {
		return repository.findAll().stream()
				.map(mapper::toModel)
				.toList();
	}
	
	@Override
	public Contact save(Contact contact) {
		ContactEntity entity = mapper.toEntity(contact);
		return mapper.toModel(repository.save(entity));
	}
	
	@Override
	public Contact delete(String nickName) {
		Optional<ContactEntity> entity = repository.findByNickName(nickName);
		if (entity.isPresent()) {
			repository.delete(entity.get());
			return mapper.toModel(entity.get());
		}
		return null;
	}
}
