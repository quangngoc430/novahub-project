package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.AccountHasSkill;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.AccountHasSkillRepository;
import vn.novahub.helpdesk.repository.SkillRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private AccountHasSkillRepository accountHasSkillRepository;

    @Override
    public Skill createSkillByAccount(Skill skill, long accountId) {
        Skill oldSkill = skillRepository.findByName(skill.getName().toLowerCase());

        if(oldSkill == null) {
            skill.setName(skill.getName().toLowerCase());
            Skill newSkill = skillRepository.save(skill);
        } else {
            AccountHasSkill accountHasSkill = new AccountHasSkill();
            accountHasSkill.setAccountId(accountId);
            accountHasSkill.setSkillId(oldSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);
        }

        return skill;
    }

    @Override
    public Skill createASkillOfACategory(Skill skill, long categoryId, HttpServletRequest request) {
        Skill oldSkill = skillRepository.findByName(skill.getName().toLowerCase());

        if(oldSkill != null){
            return oldSkill;
        }

        skill.setName(skill.getName().toLowerCase());
        skill.setCategoryId(categoryId);

        return skillRepository.save(skill);
    }

    public Skill updateSkill(Skill skill, long accountId, long skillId) {
        Skill oldSkill = skillRepository.findByName(skill.getName().toLowerCase());

        if(oldSkill == null){
            skill.setName(skill.getName().toLowerCase());
            Skill newSkill = skillRepository.save(skill);

            AccountHasSkill accountHasSkill = accountHasSkillRepository.findByAccountIdAndSkillId(accountId, skillId);
            accountHasSkill.setSkillId(newSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);

            return newSkill;
        } else {
            AccountHasSkill accountHasSkill = accountHasSkillRepository.findByAccountIdAndSkillId(accountId, skillId);
            accountHasSkill.setSkillId(oldSkill.getId());
            accountHasSkillRepository.save(accountHasSkill);

            // delete old skill if is don't belong to any accounts
            skillRepository.deleteById(skillId);

            return oldSkill;
        }
    }

    @Override
    public Skill updateSkillByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId) {
        Skill oldSkill = skillRepository.findByIdAndCategoryId(skillId, categoryId);

        if(oldSkill == null){

            // handle skill isn;t exist
            return null;
        }

        oldSkill.setName(skill.getName());
        oldSkill = skillRepository.save(oldSkill);

        return oldSkill;
    }

    @Override
    public ArrayList<Skill> getAllSkillsOfACategoryByCategoryId(long categoryId, HttpServletRequest request) {
        return skillRepository.getAllByCategoryId(categoryId);
    }

    @Override
    public Skill getSkillBySkillId(long skillId) {
        return skillRepository.findById(skillId).get();
    }

    @Override
    public Skill getASkillByCategoryIdAndSkillId(long categoryId, long skillId, HttpServletRequest request) {
        return skillRepository.findByIdAndCategoryId(skillId, categoryId);
    }

    @Override
    public void deteleASkillByCategoryIdAndSkillId(long categoryId, long skillId, HttpServletRequest request) {
        skillRepository.deleteByIdAndCategoryId(skillId, categoryId);
    }


}
