package relationaldb.service;

import java.util.List;
import java.util.Optional;

import domain.interaction.Interaction;
import domain.interaction.InteractionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import relationaldb.entity.InteractionEntity;
import relationaldb.mapper.InteractionMapper;
import relationaldb.repository.JpaInteractionRepository;

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
		return repository.findAll().stream()
				.map(mapper::toModel)
				.toList();
	}
	
	
	@Override
	public List<Interaction> findAll(String contact) {
		return repository.findByContact(contact).stream()
				.map(mapper::toModel)
				.toList();
	}
	
	
	@Override
	public Optional<Interaction> find(String id) {
		return repository.findById(id)
				.map(mapper::toModel);
	}
	
	
	@Override
	public Interaction save(Interaction interaction) {
		InteractionEntity entity = mapper.toEntity(interaction);
		return mapper.toModel(repository.save(entity));
	}
	
	
	@Override
	public Interaction delete(String id) {
		Optional<InteractionEntity> entity = repository.findById(id);
		if (entity.isPresent()) {
			repository.delete(entity.get());
			return mapper.toModel(entity.get());
		}
		return null;
	}
}
