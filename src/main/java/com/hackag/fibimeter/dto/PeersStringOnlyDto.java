package com.hackag.fibimeter.dto;

import java.util.List;

/**
 * @author Jakub Matus
 */
public class PeersStringOnlyDto {

    private List<String> peers;

    public PeersStringOnlyDto() {
    }

    public PeersStringOnlyDto(List<String> peers) {
        this.peers = peers;
    }

    public List<String> getPeers() {
        return peers;
    }

    /**
     * @param peers the peers to set
     * @see #peers
     */
    public void setPeers(List<String> peers) {
        this.peers = peers;
    }
}
