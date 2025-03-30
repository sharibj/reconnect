package filedb;

import java.io.IOException;

import domain.group.Group;
import domain.group.GroupDomainService;
import domain.group.GroupRepository;
import domain.contact.ContactRepository;

public class GroupFileService extends GroupDomainService {
    private final GroupFileRepository groupFileRepository;

    public GroupFileService(final GroupRepository groupRepository, final ContactRepository contactRepository) {
        super(groupRepository, contactRepository);
        this.groupFileRepository = (GroupFileRepository) groupRepository;
    }

    @Override
    public void add(final Group group) throws IOException {
        super.add(group);
        groupFileRepository.commit();
    }

    @Override
    public void update(final Group group) throws IOException {
        super.update(group);
        groupFileRepository.commit();
    }

    @Override
    public void remove(final String name) throws IOException {
        super.remove(name);
        groupFileRepository.commit();
    }
}
