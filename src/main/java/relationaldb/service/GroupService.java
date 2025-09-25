package relationaldb.service;

import java.util.List;
import java.util.Optional;

import domain.group.Group;
import domain.group.GroupRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import relationaldb.entity.GroupEntity;
import relationaldb.mapper.GroupMapper;
import relationaldb.repository.JpaGroupRepository;

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
		return repository.findByName(name)
				.map(mapper::toModel);
	}
	
	
	@Override
	public List<Group> findAll() {
		return repository.findAll().stream()
				.map(mapper::toModel)
				.toList();
	}
	
	
	@Override
	public Group save(Group group) {
		GroupEntity entity = mapper.toEntity(group);
		return mapper.toModel(repository.save(entity));
	}
	
	
	@Override
	public Group delete(String name) {
		Optional<GroupEntity> entity = repository.findByName(name);
		if (entity.isPresent()) {
			repository.delete(entity.get());
			return mapper.toModel(entity.get());
		}
		return null;
	}
}
