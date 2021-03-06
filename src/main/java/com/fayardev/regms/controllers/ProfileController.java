package com.fayardev.regms.controllers;

import com.fayardev.regms.controllers.abstracts.IProfileController;
import com.fayardev.regms.dtos.*;
import com.fayardev.regms.entities.BaseEntity;
import com.fayardev.regms.entities.Profile;
import com.fayardev.regms.entities.User;
import com.fayardev.regms.exceptions.UserException;
import com.fayardev.regms.exceptions.enums.ErrorComponents;
import com.fayardev.regms.exceptions.enums.Errors;
import com.fayardev.regms.services.ProfileService;
import com.fayardev.regms.services.UserService;
import com.fayardev.regms.util.HeaderUtil;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*", maxAge = 3600)
public final class ProfileController extends BaseController implements IProfileController {

    private final ProfileService profileService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, ModelMapper modelMapper, UserService userService) {
        this.profileService = profileService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    @GetMapping("/add")
    public Object addProfile(HttpServletRequest request, @RequestBody ProfileDto profileDto) throws Exception {
        var user = userService.getEntityById(Integer.parseInt(HeaderUtil.getTokenPayloadID(request)));
        if (user == null) {
            throw new UserException("User Null", Errors.NULL, ErrorComponents.USER);
        }
        Profile profile = modelMapper.map(profileDto, Profile.class);
        profile.setUser(user);
        return profileService.add(profile);
    }

    @Override
    @GetMapping("/my-profile")
    public ProfileDto getMyProfile(HttpServletRequest request) throws Exception {
        var user = userService.getEntityById(Integer.parseInt(HeaderUtil.getTokenPayloadID(request)));
        if (user == null) {
            throw new UserException("User Null", Errors.NULL, ErrorComponents.USER);
        }
        return modelMapper.map(profileService.getEntityByUser(user), ProfileDto.class);
    }

    @Override
    @PostMapping("/change-about-me")
    public boolean changeAboutMe(HttpServletRequest request, @RequestBody String aboutMe) throws Exception {
        var user = userService.getEntityById(Integer.parseInt(HeaderUtil.getTokenPayloadID(request)));
        if (user == null) {
            throw new UserException("User Null", Errors.NULL, ErrorComponents.USER);
        }
        var profile = profileService.getEntityByUser(user);
        ((Profile) profile).setAboutMe(aboutMe);

        return profileService.changeAboutMe((Profile) profile);
    }

    @Override
    @PostMapping("/update-avatar")
    public boolean updateAvatar(HttpServletRequest request, @RequestBody String base64) throws Exception {
        var user = userService.getEntityById(Integer.parseInt(HeaderUtil.getTokenPayloadID(request)));
        if (user == null) {
            throw new UserException("User Null", Errors.NULL, ErrorComponents.USER);
        }
        var profile = profileService.getEntityByUser(user);
        ((Profile) profile).setAvatarPath(base64);

        return profileService.updateAvatar((Profile) profile);
    }

    @Override
    @PostMapping("/delete-avatar")
    public boolean deleteAvatar(HttpServletRequest request) throws Exception {
        var user = userService.getEntityById(Integer.parseInt(HeaderUtil.getTokenPayloadID(request)));
        if (user == null) {
            throw new UserException("User Null", Errors.NULL, ErrorComponents.USER);
        }
        return profileService.deleteAvatar((Profile) profileService.getEntityByUser(user));
    }

    @Override
    @GetMapping("/{username}")
    public Object getProfile(HttpServletRequest request, @PathVariable String username, @RequestParam String type) throws JSONException, UserException {
        var user = userService.getEntityById(Integer.parseInt(HeaderUtil.getTokenPayloadID(request)));
        if (user == null) {
            throw new UserException("User Null", Errors.NULL, ErrorComponents.USER);
        }
        BaseEntity theUser = userService.getEntityByUsername(username);
        if (theUser.getID() == -1) {
            return false;
        }
        BaseEntity theProfile = profileService.getEntityByUser((User) theUser);
        if (theProfile.getID() == -1) {
            return false;
        }
        if (type.equals("mini")) {
            OtherUserMiniDto otherUserMiniDto = modelMapper.map(theUser, OtherUserMiniDto.class);
            OtherProfileMiniDto otherProfileMiniDto = modelMapper.map(theProfile, OtherProfileMiniDto.class);
            otherProfileMiniDto.setOtherUserMiniDto(otherUserMiniDto);
            return otherProfileMiniDto;
        }
        OtherUserDto otherUserDto = modelMapper.map(theUser, OtherUserDto.class);
        OtherProfileDto otherProfileDto = modelMapper.map(theProfile, OtherProfileDto.class);
        otherProfileDto.setOtherUserDto(otherUserDto);

        return otherProfileDto;
    }

    @Override
    @PostMapping("/timeline-get-profile")
    public Object timelineGetProfile(HttpServletRequest request, @RequestBody Map<String, Object> map) throws Exception {
        return null;
    }

    @Override
    @PostMapping("/timeline")
    public Object timeline(HttpServletRequest request, @RequestBody Map<String, Object> map) throws Exception {
        return null;
    }

    @Override
    @PostMapping("/search")
    public Object search(HttpServletRequest request, @RequestBody Map<String, Object> map) throws JSONException {
        return null;
    }
}
