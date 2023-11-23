package kr.ch10.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.ch10.dto.User1DTO;
import kr.ch10.dto.User4DTO;

@Mapper
public interface User4Mapper {
	public int insertUser4(User4DTO dto);
	public User4DTO selectUser4(String id);
	public List<User4DTO> selectUser4s();
	public int updateUser4(User4DTO dto);
	public int deleteUser4(String id);
}
