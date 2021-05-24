package org.trafficplatform.mosaicserver.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
@Getter
public class InputSourcePositionInMosaicKey implements Serializable {

	private static final long serialVersionUID = 8395694284755118730L;

	@Column(name = "mosaic_id")
	Long mosaicId;
	
	@Column(name = "input_source_id")
	Long inputSourceId;
	
	public InputSourcePositionInMosaicKey() {}
	
	public InputSourcePositionInMosaicKey(Long mosaicId, Long inputSourceId) {
		this.mosaicId = mosaicId;
		this.inputSourceId = inputSourceId;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InputSourcePositionInMosaicKey)) return false;
        InputSourcePositionInMosaicKey that = (InputSourcePositionInMosaicKey) o;
        return Objects.equals(this.mosaicId, that.getMosaicId()) &&
               Objects.equals(this.inputSourceId, that.getInputSourceId());
    }
 
	@Override
    public int hashCode() {
        return Objects.hash(this.mosaicId, this.inputSourceId);
    }
}

