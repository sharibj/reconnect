package relationaldb.service;

import java.util.List;
import java.util.Optional;

import domain.contact.Contact;
import domain.contact.ContactRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import relationaldb.entity.ContactEntity;
import relationaldb.mapper.ContactMapper;
import relationaldb.repository.JpaContactRepository;
import spring.security.TenantContext;

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
		String currentUser = TenantContext.getCurrentTenant();
		return repository.findByNickNameAndUsername(nickName, currentUser)
				.map(mapper::toModel);
	}
	
	@Override
	public List<Contact> findAll() {
		String currentUser = TenantContext.getCurrentTenant();
		return repository.findByUsername(currentUser).stream()
				.map(mapper::toModel)
				.toList();
	}
	
	@Override
	public Contact save(Contact contact) {
		String currentUser = TenantContext.getCurrentTenant();
		ContactEntity entity = mapper.toEntity(contact);
		entity.setUsername(currentUser);
		return mapper.toModel(repository.save(entity));
	}
	
	@Override
	public Contact delete(String nickName) {
		String currentUser = TenantContext.getCurrentTenant();
		Optional<ContactEntity> entity = repository.findByNickNameAndUsername(nickName, currentUser);
		if (entity.isPresent()) {
			repository.delete(entity.get());
			return mapper.toModel(entity.get());
		}
		return null;
	}
}
