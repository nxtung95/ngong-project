//package vn.ngong.entity;
//
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.Id;
//import java.io.Serializable;
//import java.util.Objects;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class MenuPostId implements Serializable {
//	@Id
//	private int menuId;
//	@Id
//	private int postId;
//
//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (!(o instanceof MenuPostId))
//			return false;
//		MenuPostId that = (MenuPostId) o;
//		return getMenuId() == that.getMenuId() && getPostId() == that.getPostId();
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(getMenuId(), getPostId());
//	}
//}
