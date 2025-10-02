package adapter.secondary.persistence.jpa.service;

import java.util.List;
import java.util.Optional;

import domain.interaction.Interaction;
import domain.interaction.InteractionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import adapter.secondary.persistence.jpa.entity.InteractionEntity;
import adapter.secondary.persistence.jpa.mapper.InteractionMapper;
import adapter.secondary.persistence.jpa.repository.JpaInteractionRepository;
import adapter.primary.http.security.TenantContext;

@Service
@Profile("prod")
public class InteractionService implements InteractionRepository {
	
	private final JpaInteractionRepository repository;
	private final InteractionMapper mapper;
	
	public InteractionService(JpaInteractionRepository repository, InteractionMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}
	
	@Override
	public List<Interaction> findAll() {
		String currentUser = TenantContext.getCurrentTenant();
		return repository.findByUsername(currentUser).stream()
				.map(mapper::toModel)
				.toList();
	}
	
	@Override
	public List<Interaction> findAll(String contact) {
		String currentUser = TenantContext.getCurrentTenant();
		return repository.findByContactAndUsername(contact, currentUser).stream()
				.map(mapper::toModel)
				.toList();
	}
	
	@Override
	public Optional<Interaction> find(String id) {
		try {
			Long longId = Long.parseLong(id);
			return repository.findById(longId)
					.filter(entity -> entity.getUsername().equals(TenantContext.getCurrentTenant()))
					.map(mapper::toModel);
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
	
	@Override
	public Interaction save(Interaction interaction) {
		String currentUser = TenantContext.getCurrentTenant();
		InteractionEntity entity = mapper.toEntity(interaction);
		entity.setUsername(currentUser);
		return mapper.toModel(repository.save(entity));
	}
	
	@Override
	public Interaction delete(String id) {
		String currentUser = TenantContext.getCurrentTenant();
		try {
			Long longId = Long.parseLong(id);
			Optional<InteractionEntity> entity = repository.findById(longId)
					.filter(e -> e.getUsername().equals(currentUser));
			if (entity.isPresent()) {
				repository.delete(entity.get());
				return mapper.toModel(entity.get());
			}
		} catch (NumberFormatException e) {
			// Invalid ID format
		}
		return null;
	}
}
