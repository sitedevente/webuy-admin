package com.univtours.webuy.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.univtours.webuy.domain.Offer;
import com.univtours.webuy.repository.OfferRepository;
import com.univtours.webuy.security.AuthoritiesConstants;
import com.univtours.webuy.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.univtours.webuy.domain.Offer}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OfferResource {

    private final Logger log = LoggerFactory.getLogger(OfferResource.class);

    private static final String ENTITY_NAME = "offer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfferRepository offerRepository;

    public OfferResource(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    /**
     * {@code POST  /offers} : Create a new offer.
     *
     * @param offer the offer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new offer, or with status {@code 400 (Bad Request)} if the offer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/offers")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Offer> createOffer(@RequestBody Offer offer) throws URISyntaxException {
        log.debug("REST request to save Offer : {}", offer);
        if (offer.getId() != null) {
            throw new BadRequestAlertException("A new offer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Offer result = offerRepository.save(offer);
        return ResponseEntity.created(new URI("/api/offers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /offers} : Updates an existing offer.
     *
     * @param offer the offer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offer,
     * or with status {@code 400 (Bad Request)} if the offer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the offer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/offers")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Offer> updateOffer(@RequestBody Offer offer) throws URISyntaxException {
        log.debug("REST request to update Offer : {}", offer);
        if (offer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Offer result = offerRepository.save(offer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, offer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /offers} : get all the offers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offers in body.
     */
    @GetMapping("/offers")
    public ResponseEntity<List<Offer>> getAllOffers(Pageable pageable) {
        log.debug("REST request to get a page of Offers");
        Page<Offer> page = offerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /offers/:id} : get the "id" offer.
     *
     * @param id the id of the offer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/offers/{id}")
    public ResponseEntity<Offer> getOffer(@PathVariable Long id) {
        log.debug("REST request to get Offer : {}", id);
        Optional<Offer> offer = offerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(offer);
    }

    /**
     * {@code DELETE  /offers/:id} : delete the "id" offer.
     *
     * @param id the id of the offer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/offers/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        log.debug("REST request to delete Offer : {}", id);

        offerRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
