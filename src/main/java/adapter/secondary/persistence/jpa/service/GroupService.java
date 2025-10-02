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
		String currentUser = TenantContext.getCurrentTenant();
		return repository.findByNameAndUsername(name, currentUser)
				.map(mapper::toModel);
	}
	
	@Override
	public List<Group> findAll() {
		String currentUser = TenantContext.getCurrentTenant();
		return repository.findByUsername(currentUser).stream()
				.map(mapper::toModel)
				.toList();
	}
	
	@Override
	public Group save(Group group) {
		String currentUser = TenantContext.getCurrentTenant();
		GroupEntity entity = mapper.toEntity(group);
		entity.setUsername(currentUser);
		return mapper.toModel(repository.save(entity));
	}
	
	@Override
	public Group delete(String name) {
		String currentUser = TenantContext.getCurrentTenant();
		Optional<GroupEntity> entity = repository.findByNameAndUsername(name, currentUser);
		if (entity.isPresent()) {
			repository.delete(entity.get());
			return mapper.toModel(entity.get());
		}
		return null;
	}
}
