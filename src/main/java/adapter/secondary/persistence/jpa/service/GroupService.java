package adapter.secondary.persistence.jpa.service;

import java.util.List;
import java.util.Optional;

import domain.group.Group;
import domain.group.GroupRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import adapter.secondary.persistence.jpa.entity.GroupEntity;
import adapter.secondary.persistence.jpa.mapper.GroupMapper;
import adapter.secondary.persistence.jpa.repository.JpaGroupRepository;
import adapter.primary.http.security.TenantContext;

@Service
@Profile("prod")
public class GroupService implements GroupRepository {
	
	private final JpaGroupRepository repository;
	private final GroupMapper mapper;
	
	public GroupService(JpaGroupRepository repository, GroupMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}
	
	@Override
	public Optional<Group> find(String name) {
		List<GroupEntity> entities = repository.findAllByName(name);
		if (entities.isEmpty()) {
			return Optional.empty();
		}
		// Return the first group if multiple exist with same name
		return Optional.of(mapper.toModel(entities.get(0)));
	}
	
	@Override
	public List<Group> findAll() {
		return repository.findAll().stream()
				.map(mapper::toModel)
				.toList();
	}
	
	@Override
	public Group save(Group group) {
		// For updates (when duplicates exist), update the first matching group
		List<GroupEntity> existingEntities = repository.findAllByName(group.getName());

		GroupEntity entity;
		if (!existingEntities.isEmpty()) {
			// Update the first existing group
			entity = existingEntities.get(0);
			entity.setFrequencyInDays(group.getFrequencyInDays());
		} else {
			// Create new group
			entity = mapper.toEntity(group);
		}

		return mapper.toModel(repository.save(entity));
	}
	
	@Override
	public Group delete(String name) {
		List<GroupEntity> entities = repository.findAllByName(name);
		if (!entities.isEmpty()) {
			// Delete the first group if multiple exist with same name
			GroupEntity entityToDelete = entities.get(0);
			repository.delete(entityToDelete);
			return mapper.toModel(entityToDelete);
		}
		return null;
	}
}
